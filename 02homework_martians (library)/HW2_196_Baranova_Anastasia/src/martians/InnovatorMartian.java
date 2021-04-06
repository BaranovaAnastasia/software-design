package martians;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Class representing innovator martians.
 * Mutable analogue of conservative.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru"> Anastasia Baranova</a>
 */
public class InnovatorMartian<T> implements Martian<T> {
    private T code;
    private InnovatorMartian<T> parent = null;
    private final Collection<InnovatorMartian<T>> children = new ArrayList<>();

    /**
     * Constructor creates martian with the given gene code..
     */
    public InnovatorMartian(T code) {
        this.code = code;
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

    /**
     * Changes martian gene code to the given.
     *
     * @param value New gene code
     */
    public void setGeneCode(T value) {
        code = value;
    }

    /**
     * Searches for the given martian in a list of descendants.
     *
     * @param innovatorMartian Martian to check
     * @return True, if martian was found in a list of descendants, false otherwise.
     */
    private boolean isDescendant(InnovatorMartian<T> innovatorMartian) {
        for (var child : getDescendants()) {
            if (child.equals(innovatorMartian)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the given martian is an ancestor.
     *
     * @return True, if martian is an ancestor, false otherwise.
     */
    private boolean isAncestor(InnovatorMartian<T> innovatorMartian) {
        if (parent == null) {
            return false;
        }
        if (parent.equals(innovatorMartian)) {
            return true;
        }
        return parent.isAncestor(innovatorMartian);
    }


    /**
     * Changes martian parent to the given martian.
     *
     * @return True, if parent successfully changed, false otherwise.
     */
    public boolean setParent(InnovatorMartian<T> newParent) {
        // Checking martian relations.
        if (this.equals(newParent) ||
                isDescendant(newParent)) {
            return false;
        }

        // Setting new parent
        if (parent != null) {
            parent.children.remove(this);
        }
        parent = newParent;
        newParent.children.add(this);
        return true;
    }

    /**
     * Changes martian children to the given list of martians.
     *
     * @return True, if parent successfully changed, false otherwise.
     */
    public boolean setChildren(Collection<Martian<T>> newChildren) {
        // Checking martian relations.
        for (var child : newChildren) {
            if (!(child instanceof InnovatorMartian) || this.equals(child) ||
                    isAncestor((InnovatorMartian<T>) child)) {
                return false;
            }
        }

        for (var child : children) {
            removeChild(child);
        }

        for (var child : newChildren) {
            ((InnovatorMartian<T>) child).setParent(this);
        }

        return true;
    }

    /**
     * Removes martian from the list of children.
     *
     * @param exChild Martian to remove
     * @return True, if martian was removed, false otherwise.
     */
    public boolean removeChild(InnovatorMartian<T> exChild) {
        for (var child : children) {
            if (child.equals(exChild)) {
                exChild.parent = null;
                break;
            }
        }
        return children.remove(exChild);
    }

    /**
     * Adds martian to the list of children.
     *
     * @param child Martian to add
     * @return True, if martian was added, false otherwise.
     */
    public boolean addChild(InnovatorMartian<T> child) {
        if (isAncestor(child)) {
            return false;
        }
        return child.setParent(this);
    }

    @Override
    public String toString() {
        String type = code.getClass().toString();
        type = type.substring(type.lastIndexOf(".") + 1);
        return "InnovatorMartian (" + type + ":" + code + ")";
    }

    /**
     * Creates immutable version of an innovator.
     *
     * @return Conservator created from this innovator.
     */
    public ConservativeMartian<T> createConservative() {
        return new ConservativeMartian<>(this);
    }
}

