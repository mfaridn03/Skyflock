package dev.farid.skyflock.features.foraging;

import dev.farid.skyflock.Skyflock;
import dev.farid.skyflock.features.Feature;
import dev.farid.skyflock.utils.LocationUtils;
import dev.farid.skyflock.utils.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class TreeSelector extends Feature {

    public Set<Node> test = new HashSet<>();

    public TreeSelector() {
        // TODO: set tree parent node from top/most central
        // TODO: new Tree class that contains surrounding wood blocks and initial number of logs, should be mined based on % gone
        // TODO: shift to update player location. Highlight trees that are a certain blocks away from player (figure it out manually)
        super("Birch Tree Route Helper");
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (!getConfigStatus() || LocationUtils.location == null || !LocationUtils.location.equals("Savanna Woodland"))
            return;

        test.add(new Node(-426, 111, 19));
        test.add(new Node(-432, 111, -23));
        test.add(new Node(-416, 111, -22));
        test.add(new Node(-414, 111, -6));
        test.add(new Node(-426, 115, 1));
        test.add(new Node(-425, 111, -12));
        test.add(new Node(-437, 112, 30));
        test.add(new Node(-450, 112, 15));
        test.add(new Node(-450, 112, 0));
        test.add(new Node(-449, 113, -30));
        test.add(new Node(-455, 114, -43));
        test.add(new Node(-400, 113, -9));
        test.add(new Node(-385, 113, -7));
        test.add(new Node(-387, 112, 9));
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!getConfigStatus() || LocationUtils.location == null || !LocationUtils.location.equals("Savanna Woodland"))
            return;

        GlStateManager.disableDepth();

        for (Node n : this.test) {
            n.updateSurrounding();
            AxisAlignedBB parent = RenderUtils.Render3D.blockPosToAABB(n.pos());
            RenderUtils.Render3D.drawFilledBoundingBox(parent, new Color(255, 0, 255, 100), event.partialTicks, true);

            for (Node c : n.surrounding) {
                AxisAlignedBB child = RenderUtils.Render3D.blockPosToAABB(c.pos());
                RenderUtils.Render3D.drawFilledBoundingBox(child, new Color(255, 0, 255, 100), event.partialTicks, true);
            }
        }
        GlStateManager.enableDepth();
    }

    @Override
    public boolean getConfigStatus() {
        return Skyflock.config.birchTreeSelector;
    }

    // TODO: cache every birch wood location?
    public class Node {
        public int x;
        public int y;
        public int z;
        public boolean isMined;
        public Set<Node> surrounding = new HashSet<>(); // for wood scanning
        public Set<Node> neighbours = new HashSet<>(); // for pathing

        public Node(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.isMined = false;
        }

        public BlockPos pos() {
            return new BlockPos(this.x, this.y, this.z);
        }

        public void updateSurrounding() {
            // check the block that is x+1, y+1, z+1, x-1, y-1, z-1 from original.
            // if it is acacia wood, add that to this.surrounding and redo the scan.
            // Repeat until it reaches the block that does not have any surrounding unchecked woods
            this.surrounding.clear();
            Set<Node> checked = new HashSet<>();
            Stack<Node> toCheck = new Stack<>();
            toCheck.add(this);

            while (!toCheck.isEmpty()) {
                Node current = toCheck.pop();
                if (checked.contains(current))
                    continue;
                checked.add(current);

                for (Node n : getAdjacentWood(current)) {
                    if (!checked.contains(n) && isAcaciaWood(n)) {
                        this.surrounding.add(n);
                        toCheck.add(n);
                    }
                }
            }
        }

        private Set<Node> getAdjacentWood(Node node) {
            Set<Node> neighbors = new HashSet<>();

            // x
            neighbors.add(new Node(node.x + 1, node.y, node.z));
            neighbors.add(new Node(node.x - 1, node.y, node.z));

            // y
            neighbors.add(new Node(node.x, node.y + 1, node.z));
            neighbors.add(new Node(node.x, node.y - 1, node.z));

            // z
            neighbors.add(new Node(node.x, node.y, node.z + 1));
            neighbors.add(new Node(node.x, node.y, node.z - 1));
            return neighbors;
        }

        private boolean isAcaciaWood(Node n) {
            IBlockState state = mc.theWorld.getBlockState(n.pos());
            Block block = state.getBlock();

            if (block instanceof BlockNewLog) {
                BlockPlanks.EnumType variant = state.getValue(BlockNewLog.VARIANT);
                return variant == BlockPlanks.EnumType.ACACIA;
            }
            return false;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Node))
                return false;

            Node n = (Node) o;
            return n.x == this.x && n.y == this.y && n.z == this.z;
        }

        @Override
        public int hashCode() {
            return 31 * (31 * this.x + this.y) + this.z;
        }

        @Override
        public String toString() {
            return "Node(x=" + this.x + ", y=" + this.y + ", z=" + this.z + ")";
        }
    }
}
