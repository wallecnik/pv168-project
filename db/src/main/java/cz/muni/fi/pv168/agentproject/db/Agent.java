package cz.muni.fi.pv168.agentproject.db;

import java.time.Instant;

/**
 * This class represents a secret agent. It's just a holder of few attributes like structure in C.
 *
 * @author  Wallecnik
 * @version 31.2.2015
 */
public class Agent implements Comparable<Agent> {

    private Long id;
    private String name;
    private Instant born;

    /**
     * Constructor for objects of class Agent.
     *
     * @param id          synthetic id of this Agent
     * @param name        name of this Agent
     * @param born        long value of time in miliseconds
     */
    public Agent(Long id, String name, Instant born) {
        this.id = id;
        this.name = name;
        this.born = born;
    }

    /**
     * Getters
     */

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Instant getBorn() {
        return born;
    }

    /**
     * Setters
     */

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBorn(Instant born) {
        this.born = born;
    }

    /**
     * Equals + hashCode methods
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Agent)) return false;

        Agent agent = (Agent) o;

        if (born != null ? !born.equals(agent.born) : agent.born != null) return false;
        if (id != null ? !id.equals(agent.id) : agent.id != null) return false;
        if (name != null ? !name.equals(agent.name) : agent.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (born != null ? born.hashCode() : 0);
        return result;
    }

    /**
     * Returns a brief description of this agent. Exact format is unspecified,
     * but the following may look typically like that:
     *
     * "Agent{name='John Doe', born=2015-03-25T08:44:05.143Z}"
     *
     * @return String representing this Agent
     */
    @Override
    public String toString() {
        return "Agent{" +
                "name='" + name + '\'' +
                ", born=" + born +
                '}';
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     * <p>
     * <p>The implementor must ensure <tt>sgn(x.compareTo(y)) ==
     * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
     * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
     * <tt>y.compareTo(x)</tt> throws an exception.)
     * <p>
     * <p>The implementor must also ensure that the relation is transitive:
     * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
     * <tt>x.compareTo(z)&gt;0</tt>.
     * <p>
     * <p>Finally, the implementor must ensure that <tt>x.compareTo(y)==0</tt>
     * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
     * all <tt>z</tt>.
     * <p>
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
     * class that implements the <tt>Comparable</tt> interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     * <p>
     * <p>In the foregoing description, the notation
     * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
     * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
     * <tt>0</tt>, or <tt>1</tt> according to whether the value of
     * <i>expression</i> is negative, zero or positive.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    public int compareTo(Agent o) {
        if (o.id == null) {
            throw new IllegalArgumentException("Agent's id to be compared to has null id");
        }
        return this.id.compareTo(o.id);
    }
}



















