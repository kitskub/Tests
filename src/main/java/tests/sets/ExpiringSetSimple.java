package tests.sets;

import java.util.AbstractSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class ExpiringSetSimple<T> extends AbstractSet<T> {

    private HashSet<Timed<T>> timedSet = new HashSet<Timed<T>>();
    private HashSet<T> set = new HashSet<T>();
    private final long delayinMillis;

    public ExpiringSetSimple(long delay, TimeUnit sourceUnit) {
        this.delayinMillis = TimeUnit.MILLISECONDS.convert(delay, sourceUnit);
    }

    @Override
    public Iterator<T> iterator() {
        cleanUp();
        return set.iterator();
    }

    @Override
    public int size() {
        cleanUp();
        return set.size();
    }

    @Override
    public boolean add(T e) {
        cleanUp();
        Timed<T> obj = new Timed<T>(e, delayinMillis, TimeUnit.MILLISECONDS);
        timedSet.remove(obj);
        timedSet.add(obj);
        return set.add(e);
    }

    private void cleanUp() {
        Iterator<Timed<T>> it = timedSet.iterator();
        while (it.hasNext()) {
            Timed<T> next = it.next();
            if (next.isExpired()) set.remove(next.object);
        }
    }
    
    private static class Timed<T> {
        private final T object;
        private final long finishTime;

        public Timed(T object, long time, TimeUnit unit) {
            this.object = object;
            this.finishTime = System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(time, unit);
        }
        
        public boolean isExpired() {
            return System.currentTimeMillis() - finishTime >= 0;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 97 * hash + (this.object != null ? this.object.hashCode() : 0);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Timed<T> other = (Timed<T>) obj;
            if (this.object != other.object && (this.object == null || !this.object.equals(other.object))) {
                return false;
            }
            return true;
        }
    }
}
