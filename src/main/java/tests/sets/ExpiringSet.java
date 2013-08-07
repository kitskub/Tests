package tests.sets;

import java.util.AbstractSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.builder.CompareToBuilder;

public class ExpiringSet<T> extends AbstractSet<T> {

    private DelayQueue<DelayedObject<T>> removeQueue = new DelayQueue<DelayedObject<T>>();
    private HashSet<T> set = new HashSet<T>();
    private final long delayinMillis;

    public ExpiringSet(long delay, TimeUnit sourceUnit) {
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
        DelayedObject<T> obj = new DelayedObject<T>(delayinMillis, TimeUnit.MILLISECONDS, e);
        removeQueue.remove(obj);
        removeQueue.add(obj);
        return set.add(e);
    }

    private void cleanUp() {
        DelayedObject<T> o;
        while ((o = removeQueue.poll()) != null) {
            set.remove(o.getObject());
        }
    }
    
    private static class DelayedObject<T> implements Delayed {
        private final long finishTime;
        private final T object;

        public DelayedObject(long delay, TimeUnit unit, T object) {
            finishTime = System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(delay, unit);
            this.object = object;
        }

        public long getDelay(TimeUnit unit) {
            return unit.convert(finishTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        public int compareTo(Delayed o) {
            if (this == o) return 0;
            if (o instanceof DelayedObject && ((DelayedObject) o).object.getClass().isAssignableFrom(object.getClass())) {
                DelayedObject<? extends T> other = (DelayedObject<? extends T>) o;
                return new CompareToBuilder().append(object, other.object).toComparison();
            }
            if (o == null) {
                return 1;
            }
            return 0;
        }

        public T getObject() {
            return object;
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
            final DelayedObject<T> other = (DelayedObject<T>) obj;
            if (this.object != other.object && (this.object == null || !this.object.equals(other.object))) {
                return false;
            }
            return true;
        }
    }
}
