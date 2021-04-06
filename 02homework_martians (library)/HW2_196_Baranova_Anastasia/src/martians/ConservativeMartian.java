package martians;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Class representing conservative martians.
 * Immutable analogue of innovators.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru"> Anastasia Baranova</a>
 */
public class ConservativeMartian<T> implements Martian<T> {
    final private T code;
    final private ConservativeMartian<T> parent;
    final private Collection<Martian<T>> children;

    // Helper variables for building conservative martian from innovator.
    // Innovator becoming conservative.
    private InnovatorMartian<T> source = null;
    // Top ancestor.
    private InnovatorMartian<T> root = null;
    // This martian parent.
    private ConservativeMartian<T> preParent = null;

    /**
     * Class constructor converting innovator martian to its immutable analogue.
     * Creates conservative martian from innovator.
     *
     * @param innovator Innovator martian decided to become a conservative
     * @throws NullPointerException If innovator is null.
     */
    public ConservativeMartian(InnovatorMartian<T> innovator) {
        if (innovator == null) {
            throw new NullPointerException("innovator was null");
        }
        source = innovator;
        findRoot();

        // Making ancestors conservative.
        buildAncestors();

        // Initializing martian data.
        this.code = innovator.getCode();
        this.parent = preParent;
        if (parent != null) {
            parent.children.add(this);
        }
        children = new ArrayList<>();

        // Making descendants conservative.
        buildDescendants(innovator);

        // Clear helpers.
        source = null;
        root = null;
        preParent = null;
    }

    /**
     * Class constructor.
     * Creates conservative martian with this gene code and parent
     * and empty list of children.
     *
     * @param code   Martian gene code
     * @param parent Martian parent
     */
    private ConservativeMartian(T code, ConservativeMartian<T> parent) {
        this.code = code;
        this.parent = parent;
        children = new ArrayList<>();
    }

    /**
     * Finds top ancestor and puts in to root variable.
     */
    private void findRoot() {
        root = source;
        while (root.getParent() != null) {
            root = (InnovatorMartian<T>) root.getParent();
        }
    }

    /**
     * Converts all of the ancestors
     * (and all of the family members who are not this martian descendant)
     * into conservative martians.
     */
    private void buildAncestors() {
        createAncestors(root, null, source);
    }

    /**
     * Converts all of the descendants into conservative martians.
     *
     * @param innovator An innovator whose descendants become conservatives
     */
    private void buildDescendants(InnovatorMartian<T> innovator) {
        for (var child : innovator.getChildren()) {
            children.add(createDescendants((InnovatorMartian<T>) child, this));
        }
    }

    /**
     * Recursively fills genealogical tree of conservatives, created from innovators.
     *
     * @param innovator Current tree node
     * @param parent    Current node parent
     * @param target    Innovator martian decided to become a conservative.
     *                  Its descendants and it itself are processed separately.
     */
    private ConservativeMartian<T> createAncestors(InnovatorMartian<T> innovator,
                                                   ConservativeMartian<T> parent,
                                                   InnovatorMartian<T> target) {
        // Checking if it is an initial martian.
        if (innovator.equals(target)) {
            preParent = parent;
            return null;
        }
        // Creating an ancestor.
        var root = new ConservativeMartian<>(innovator.getCode(), parent);

        // Adding all the children if it has them.
        if (innovator.getChildren() == null) {
            return root;
        }

        for (var child : innovator.getChildren()) {
            var childToAdd = createAncestors((InnovatorMartian<T>) child, root, target);
            if (childToAdd != null) {
                root.children.add(childToAdd);
            }
        }

        return root;
    }

    /**
     * Recursively fills genealogical tree of conservatives, created from innovators.
     *
     * @param innovator Current tree node
     * @param parent    Current node parent
     */
    private ConservativeMartian<T> createDescendants(InnovatorMartian<T> innovator,
                                                     ConservativeMartian<T> parent) {
        // Creating an descendant.
        var root = new ConservativeMartian<>(innovator.getCode(), parent);

        // Adding all the children if it has them.
        if (innovator.getChildren() == null) {
            return root;
        }

        for (var child : innovator.getChildren()) {
            root.children.add(createDescendants((InnovatorMartian<T>) child, root));
        }
        return root;
    }


    @Override
    public T getCode() {
        return code;
    }

    @Override
    public Martian<T> getParent() {
        return parent;
    }

    @Override
    public Collection<Martian<T>> getChildren() {
        return Collections.unmodifiableCollection(children);
    }

    @Override
    public Collection<Martian<T>> getDescendants() {
        var descendants = new ArrayList<Martian<T>>();
        for (var child : children) {
            descendants.add(child);
            descendants.addAll(child.getDescendants());
        }
        return Collections.unmodifiableCollection(descendants);
    }

    @Override
    public boolean hasChildWithValue(T value) {
        for (var child : children) {
            if (child.getCode().equals(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasDescendantWithValue(T value) {
        for (var child : getDescendants()) {
            if (child.getCode().equals(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String giveReport() {
        return new GenealogicalTree(this).getReport();
    }

    @Override
    public String toString() {
        String type = code.getClass().toString();
        type = type.substring(type.lastIndexOf(".") + 1);
        return "ConservativeMartian (" + type + ":" + code + ")";
    }
}
