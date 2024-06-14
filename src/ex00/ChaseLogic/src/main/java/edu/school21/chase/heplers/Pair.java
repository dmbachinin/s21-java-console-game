package edu.school21.chase.heplers;

public class Pair <F,S> {
    private F first;
    private S second;

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public F getFirst() {
        return first;
    }

    public void setFirst(F first) {
        this.first = first;
    }

    public S getSecond() {
        return second;
    }

    public void setSecond(S second) {
        this.second = second;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Pair) {
            return this.getFirst() == ((Pair<?, ?>)obj).getFirst() &&
                    this.getSecond() == ((Pair<?, ?>)obj).getSecond();
        }
        return false;
    }

    @Override
    public String toString() {
        return "Pair{" + "first=" + first + ", second=" + second + '}';
    }

}
