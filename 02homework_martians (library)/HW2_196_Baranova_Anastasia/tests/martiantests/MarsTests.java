package martiantests;

import martians.ConservativeMartian;
import martians.GenealogicalTree;
import martians.InnovatorMartian;
import martians.Martian;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class MarsTests {
    private ArrayList<Martian<Integer>> getSimpleMartiansCollection() {
        var children = new ArrayList<Martian<Integer>>();
        var ch1 = new InnovatorMartian<>(11);
        var ch2 = new InnovatorMartian<>(12);
        var ch3 = new InnovatorMartian<>(13);
        var ch4 = new InnovatorMartian<>(14);
        children.add(ch1);
        children.add(ch2);
        children.add(ch3);
        children.add(ch4);

        return children;
    }

    private InnovatorMartian<Integer> getInnovatorForDescendants() {
        var innovatorParent = new InnovatorMartian<>(0);

        var ch1 = new InnovatorMartian<>(1);
        var ch2 = new InnovatorMartian<>(2);

        innovatorParent.addChild(ch1);
        innovatorParent.addChild(ch2);

        ch1.addChild(new InnovatorMartian<>(3));
        ch1.addChild(new InnovatorMartian<>(4));

        ch2.addChild(new InnovatorMartian<>(5));
        ch2.addChild(new InnovatorMartian<>(6));

        return innovatorParent;
    }

    private void checkDescendants(Martian<Integer> martian) {
        assertEquals(martian.getDescendants().size(), 6);

        assertTrue(martian.hasDescendantWithValue(1));
        assertTrue(martian.hasDescendantWithValue(2));
        assertTrue(martian.hasDescendantWithValue(3));
        assertTrue(martian.hasDescendantWithValue(4));
        assertTrue(martian.hasDescendantWithValue(5));
        assertTrue(martian.hasDescendantWithValue(6));
    }

    @Nested
    class TestInnovators {
        @Test
        void getCode() {
            var innovatorString = new InnovatorMartian<>("str1");
            assertEquals(innovatorString.getCode(), "str1");
        }

        @Test
        void setCode() {
            var innovatorString = new InnovatorMartian<>("str1");
            innovatorString.setGeneCode("str2");
            assertEquals(innovatorString.getCode(), "str2");
        }

        @Test
        void setParent() {
            var innovatorParent = new InnovatorMartian<>("parent");
            var innovatorChild = new InnovatorMartian<>("child");

            assertTrue(innovatorChild.setParent(innovatorParent));
        }

        @Test
        void getParent() {
            var innovatorParent = new InnovatorMartian<>("parent");
            var innovatorChild = new InnovatorMartian<>("child");

            innovatorChild.setParent(innovatorParent);

            assertEquals(innovatorChild.getParent(), innovatorParent);
        }

        @Test
        void setParentCheck() {
            var innovatorParent = new InnovatorMartian<>("parent");
            var innovatorChild = new InnovatorMartian<>("child");

            innovatorChild.setParent(innovatorParent);

            assertEquals(innovatorChild.getParent(), innovatorParent);
            assertEquals(innovatorParent.getChildren().size(), 1);
            assertTrue(innovatorParent.getChildren().contains(innovatorChild));
        }

        @Test
        void setParentItself() {
            var innovatorParent = new InnovatorMartian<>("parent");

            assertFalse(innovatorParent.setParent(innovatorParent));
        }

        @Test
        void setParentChild() {
            var innovatorParent = new InnovatorMartian<>("parent");
            var innovatorChild = new InnovatorMartian<>("child");

            innovatorChild.setParent(innovatorParent);

            assertFalse(innovatorParent.setParent(innovatorChild));
        }

        @Test
        void setParentHasParent() {
            var parent0 = new InnovatorMartian<>(0);
            var parent1 = new InnovatorMartian<>(1);

            var child = new InnovatorMartian<>(3);

            child.setParent(parent0);
            assertTrue(child.setParent(parent1));

            assertEquals(parent1, child.getParent());
            assertEquals(parent1.getChildren().size(), 1);
            assertTrue(parent0.getChildren().isEmpty());
        }

        @Test
        void setParentDescendant() {
            var parent = new InnovatorMartian<>("parent");

            var child1 = new InnovatorMartian<>("ch1");
            var child2 = new InnovatorMartian<>("ch2");
            var child3 = new InnovatorMartian<>("ch3");

            child1.setParent(parent);
            child2.setParent(child1);
            child3.setParent(child2);

            assertFalse(child1.setParent(child3));
        }

        @Test
        void getChildren() {
            var innovatorParent = new InnovatorMartian<>(0);
            innovatorParent.addChild(new InnovatorMartian<>(11));
            innovatorParent.addChild(new InnovatorMartian<>(12));
            innovatorParent.addChild(new InnovatorMartian<>(13));
            innovatorParent.addChild(new InnovatorMartian<>(14));

            assertEquals(innovatorParent.getChildren().size(), 4);

            int i = 1;
            for (var child : innovatorParent.getChildren()) {
                assertEquals(child.getCode(), 10 + i);
                assertEquals(child.getParent(), innovatorParent);
                i++;
            }
        }

        @Test
        void setChildren() {
            var innovatorParent = new InnovatorMartian<>(0);
            var children = getSimpleMartiansCollection();

            assertTrue(innovatorParent.setChildren(children));

            assertEquals(innovatorParent.getChildren().size(), 4);

            int i = 1;
            for (var child : innovatorParent.getChildren()) {
                assertEquals(child.getCode(), 10 + i);
                assertEquals(child.getParent(), innovatorParent);
                i++;
            }
        }

        @Test
        void setChildrenItself() {
            var innovatorParent = new InnovatorMartian<>(0);
            ArrayList<Martian<Integer>> children = new ArrayList<>();
            children.add(new InnovatorMartian<>(10));
            children.add(new InnovatorMartian<>(11));
            children.add(innovatorParent);
            children.add(new InnovatorMartian<>(12));

            assertFalse(innovatorParent.setChildren(children));
        }

        @Test
        void setChildrenConservative() {
            var innovatorParent = new InnovatorMartian<>(0);
            ArrayList<Martian<Integer>> children = new ArrayList<>();
            children.add(new InnovatorMartian<>(1));
            children.add(new InnovatorMartian<>(2));
            children.add(new ConservativeMartian<>(new InnovatorMartian<>(19)));
            children.add(new InnovatorMartian<>(3));

            assertFalse(innovatorParent.setChildren(children));
        }

        @Test
        void setChildrenAncestor() {
            var parent0 = new InnovatorMartian<>(0);
            var parent1 = new InnovatorMartian<>(1);

            var children = getSimpleMartiansCollection();
            children.add(parent0);

            parent1.setParent(parent0);

            assertFalse(parent1.setChildren(children));
        }

        @Test
        void getDescendants() {
            checkDescendants(getInnovatorForDescendants());
        }

        @Test
        void removeChild() {
            var innovatorParent = new InnovatorMartian<>(0);
            var child = new InnovatorMartian<>(1);

            innovatorParent.addChild(child);

            assertTrue(innovatorParent.removeChild(child));
        }

        @Test
        void removeChildCheck() {
            var innovatorParent = new InnovatorMartian<>(0);
            var child = new InnovatorMartian<>(1);

            innovatorParent.addChild(child);

            innovatorParent.removeChild(child);

            assertNotEquals(innovatorParent, child.getParent());
            assertTrue(innovatorParent.getChildren().isEmpty());
        }

        @Test
        void removeChildFalse() {
            var innovatorParent = new InnovatorMartian<>(0);
            var collection = getSimpleMartiansCollection();

            assertFalse(innovatorParent.removeChild(new InnovatorMartian<>(13)));
            assertFalse(innovatorParent.removeChild(innovatorParent));
        }

        @Test
        void addChild() {
            var innovatorParent = new InnovatorMartian<>(0);
            var child = new InnovatorMartian<>(1);

            assertTrue(innovatorParent.addChild(child));
        }

        @Test
        void addChildCheck() {
            var innovatorParent = new InnovatorMartian<>(0);
            var child = new InnovatorMartian<>(1);

            innovatorParent.addChild(child);

            assertEquals(innovatorParent, child.getParent());
            assertEquals(innovatorParent.getChildren().size(), 1);
            assertTrue(innovatorParent.hasChildWithValue(1));
        }

        @Test
        void addChildFalse() {
            var innovatorParent = new InnovatorMartian<>(0);
            var collection = getSimpleMartiansCollection();

            innovatorParent.setChildren(collection);

            assertFalse(innovatorParent.addChild(innovatorParent));
            assertFalse(((InnovatorMartian<Integer>) collection.get(0)).addChild(innovatorParent));
        }

        @Test
        void hasChildWithValueFalse() {
            var innovatorParent = new InnovatorMartian<>(0);
            var collection = getSimpleMartiansCollection();

            innovatorParent.setChildren(collection);

            assertFalse(innovatorParent.hasChildWithValue(1233));
        }

        @Test
        void hasDescendantWithValueFalse() {
            var innovatorParent = new InnovatorMartian<>(0);
            var collection = getSimpleMartiansCollection();

            innovatorParent.setChildren(collection);

            assertFalse(innovatorParent.hasDescendantWithValue(1233));
        }
    }

    @Nested
    class TestConservatives {
        @Test
        void getCode() {
            var innovator = new InnovatorMartian<>("str1");

            var conservative = innovator.createConservative();

            assertEquals(conservative.getCode(), "str1");
        }

        @Test
        void getParent() {
            var innovatorParent = new InnovatorMartian<>("parent");
            var innovatorChild = new InnovatorMartian<>("child");

            innovatorChild.setParent(innovatorParent);

            var conservative = innovatorChild.createConservative();

            assertEquals(conservative.getParent().getCode(), "parent");
            assertNull(conservative.getParent().getParent());
        }

        @Test
        void getChildren() {
            var innovatorParent = new InnovatorMartian<>(0);
            var children = getSimpleMartiansCollection();

            innovatorParent.setChildren(children);

            var conservative = innovatorParent.createConservative();

            assertEquals(conservative.getChildren().size(), 4);

            for (var child : conservative.getChildren()) {
                assertEquals(child.getChildren().size(), 0);
                assertEquals(child.getParent(), conservative);
            }
        }

        @Test
        void getDescendants() {
            var innovator = getInnovatorForDescendants();
            checkDescendants(innovator.createConservative());
        }

        @Test
        void fromInnovator() {
            String innovatorReport = "InnovatorMartian (String:0)\n" +
                    "    InnovatorMartian (String:11)\n" +
                    "        InnovatorMartian (String:111)\n" +
                    "        InnovatorMartian (String:112)\n" +
                    "            InnovatorMartian (String:1121)\n" +
                    "                InnovatorMartian (String:1121)\n" +
                    "            InnovatorMartian (String:1121)\n" +
                    "            InnovatorMartian (String:1122)\n" +
                    "    InnovatorMartian (String:12)\n" +
                    "    InnovatorMartian (String:13)\n" +
                    "        InnovatorMartian (String:131)\n" +
                    "        InnovatorMartian (String:132)\n" +
                    "            InnovatorMartian (String:1321)\n";

            var innovator
                    = (Martian<String>) GenealogicalTree.parseReport(innovatorReport).getRoot();

            var conservative = ((InnovatorMartian<String>) innovator).createConservative();

            String conservativeReport = new GenealogicalTree(conservative).getReport();

            assertEquals(conservativeReport, innovatorReport.replaceAll("InnovatorMartian",
                    "ConservativeMartian"));
        }

        @Test
        void hasChildWithValueFalse() {
            var innovatorParent = new InnovatorMartian<>(0);
            var collection = getSimpleMartiansCollection();

            innovatorParent.setChildren(collection);

            var conservative = innovatorParent.createConservative();

            assertFalse(conservative.hasChildWithValue(1233));
        }

        @Test
        void hasDescendantWithValueFalse() {
            var innovatorParent = new InnovatorMartian<>(0);
            var collection = getSimpleMartiansCollection();

            innovatorParent.setChildren(collection);

            var conservative = innovatorParent.createConservative();

            assertFalse(conservative.hasDescendantWithValue(1233));
        }
    }

    @Nested
    class TestReports {
        @Test
        void giveReport() {
            String requiredReportInnovator = "InnovatorMartian (Integer:0)\n" +
                    "    InnovatorMartian (Integer:11)\n" +
                    "        InnovatorMartian (Integer:111)\n" +
                    "    InnovatorMartian (Integer:12)\n" +
                    "    InnovatorMartian (Integer:13)\n" +
                    "        InnovatorMartian (Integer:131)\n" +
                    "            InnovatorMartian (Integer:1311)\n";

            var root = new InnovatorMartian<>(0);
            var child11 = new InnovatorMartian<>(11);
            child11.addChild(new InnovatorMartian<>(111));
            root.addChild(child11);
            root.addChild(new InnovatorMartian<>(12));
            var child13 = new InnovatorMartian<>(13);
            var child131 = new InnovatorMartian<>(131);
            child13.addChild(child131);
            child131.addChild(new InnovatorMartian<>(1311));
            root.addChild(child13);

            assertEquals(root.giveReport(), requiredReportInnovator);
            assertEquals(child13.giveReport(), requiredReportInnovator);
            assertEquals(child131.giveReport(), requiredReportInnovator);

            assertEquals(child131.createConservative().giveReport(),
                    requiredReportInnovator.replaceAll("InnovatorMartian",
                            "ConservativeMartian"));
        }

        @Test
        void fromReportInvalidReports() {
            String reportInnovator = "InnovatorMartian (Integer:0)\n" +
                    "    InnovatorMartian (Integer:11)\n" +
                    "        InnovatorMartian (Integer:111)\n" +
                    "    InnovatorMartian (Integer:12)\n" +
                    "InnovatorMartian (Integer:13)\n" +
                    "        InnovatorMartian (Integer:131)\n" +
                    "            InnovatorMartian (Integer:1311)\n";

            String finalReportInnovator = reportInnovator;
            assertThrows(IllegalArgumentException.class,
                    () -> GenealogicalTree.parseReport(finalReportInnovator));

            reportInnovator = "InnovatorMartian (Double:0)\n" +
                    "    InnovatorMartian (Double:11)\n" +
                    "        InnovatorMartian (Double:111)\n" +
                    "    InnovatorMartian (Double:12)\n" +
                    "    InnovatorMartian ()" +
                    "        InnovatorMartian (Double:131)\n" +
                    "            InnovatorMartian (Double:1311)\n";

            String finalReportInnovator1 = reportInnovator;
            assertThrows(IllegalArgumentException.class,
                    () -> GenealogicalTree.parseReport(finalReportInnovator1));

            reportInnovator = "InnovatorMartian (Integer:0)\n" +
                    "    InnovatorMartian (Integer:11)\n" +
                    "        InnovatorMartian (Integer:111)\n" +
                    "    InnovatorMartian (String:12)\n" +
                    "    InnovatorMartian (Integer:13)\n" +
                    "        InnovatorMartian (Integer:131)\n" +
                    "            InnovatorMartian (Integer:1311)\n";

            String finalReportInnovator2 = reportInnovator;
            assertThrows(IllegalArgumentException.class,
                    () -> GenealogicalTree.parseReport(finalReportInnovator2));

            reportInnovator = "InnovatorMartian (Integer:0)\n" +
                    "    InnovatorMartian (Integer:11)\n" +
                    "        InnovatorMartian (Integer:111)\n" +
                    "    InnovatorMartian (Integer:12)\n" +
                    "    InnovatorMartian (Integer:13)\n" +
                    "        ConservativeMartian (Integer:131)\n" +
                    "            InnovatorMartian (Integer:1311)\n";

            String finalReportInnovator3 = reportInnovator;
            assertThrows(IllegalArgumentException.class,
                    () -> GenealogicalTree.parseReport(finalReportInnovator3));

            reportInnovator = "InnovatorMartian (Integer:0)\n" +
                    "    InnovatorMartian (Integer:11)\n" +
                    "        InnovatorMartian (Integer:111)\n" +
                    "    InnovatorMartian (Integer:12)\n" +
                    "    \n" +
                    "        InnovatorMartian (Integer:131)\n" +
                    "            InnovatorMartian (Integer:1311)\n";

            String finalReportInnovator4 = reportInnovator;
            assertThrows(IllegalArgumentException.class,
                    () -> GenealogicalTree.parseReport(finalReportInnovator4));

            reportInnovator = "InnovatorMartian (Integer:0)\n" +
                    "    InnovatorMartian (Integer:11)\n" +
                    "        InnovatorMartian (Integer:111)\n" +
                    "    InnovatorMartian (Integer:12)\n" +
                    "  InnovatorMartian (Integer:13)\n" +
                    "        InnovatorMartian (Integer:131)\n" +
                    "            InnovatorMartian (Integer:1311)\n";

            String finalReportInnovator5 = reportInnovator;
            assertThrows(IllegalArgumentException.class,
                    () -> GenealogicalTree.parseReport(finalReportInnovator5));

            assertThrows(NullPointerException.class,
                    () -> GenealogicalTree.parseReport(null));
        }

        @Test
        void fromReportValid() {
            String report = "ConservativeMartian (Double:-7.19)\n" +
                    "    ConservativeMartian (Double:11.3)\n" +
                    "        ConservativeMartian (Double:111.11)\n" +
                    "    ConservativeMartian (Double:12.9)\n" +
                    "    ConservativeMartian (Double:13.0)\n" +
                    "        ConservativeMartian (Double:131.42)\n" +
                    "            ConservativeMartian (Double:1311.3)\n" +
                    "    ConservativeMartian (Double:14.12)\n";

            var martian
                    = (Martian<Double>) GenealogicalTree.parseReport(report).getRoot();

            assertTrue(martian instanceof ConservativeMartian);

            assertTrue(martian.getCode().equals(-7.19));

            assertEquals(martian.getChildren().size(), 4);
            assertEquals(martian.getDescendants().size(), 7);

            assertTrue(martian.hasChildWithValue(11.3));
            assertTrue(martian.hasChildWithValue(12.9));
            assertTrue(martian.hasChildWithValue(13.0));
            assertTrue(martian.hasChildWithValue(14.12));

            assertTrue(martian.hasDescendantWithValue(11.3));
            assertTrue(martian.hasDescendantWithValue(111.11));
            assertTrue(martian.hasDescendantWithValue(12.9));
            assertTrue(martian.hasDescendantWithValue(13.0));
            assertTrue(martian.hasDescendantWithValue(131.42));
            assertTrue(martian.hasDescendantWithValue(1311.3));
            assertTrue(martian.hasDescendantWithValue(14.12));

            assertFalse(martian.hasDescendantWithValue(18.0));
            assertFalse(martian.hasDescendantWithValue(23.0));
            assertFalse(martian.hasChildWithValue(10.0));
            assertFalse(martian.hasChildWithValue(-4.0));

            String r = martian.giveReport();
            assertTrue(r.equals(report));
        }
    }
}
