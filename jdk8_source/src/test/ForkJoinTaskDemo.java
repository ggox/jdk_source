package test;

import java.util.concurrent.CountedCompleter;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author: xuhui
 * @description:
 * @date: 2020/12/21 5:58 下午
 */
public class ForkJoinTaskDemo {

    public static void main(String[] args) {
        //test_status();
        test_counted_completer();
    }

    static void test_status() {
        final int NORMAL = 0xf0000000;
        final int CANCELLED = 0xc0000000;
        // tips:计算机一般以补码的形式保存
        System.out.println(CANCELLED < NORMAL);
        System.out.println(NORMAL);
        System.out.println(CANCELLED);
    }

    static void test_counted_completer() {
        String[] array = new String[] {"123", "456", "789"};
        MyOperation<String> operation = new MyOperation<>();
        ForEach.forEach(array, operation);
    }

    static class MyOperation<E> {

        void apply(E e) {
            System.out.println(e);
        }
    }

    static class ForEach<E> extends CountedCompleter<Void> {

        public static <E> void forEach(E[] array, MyOperation<E> op) {
            new ForEach<E>(null, array, op, 0, array.length).invoke();
        }

        final E[]            array;
        final MyOperation<E> op;
        final int            lo, hi;

        ForEach(CountedCompleter<?> p, E[] array, MyOperation<E> op, int lo, int hi){
            super(p);
            this.array = array;
            this.op = op;
            this.lo = lo;
            this.hi = hi;
        }

        public void compute() { // version 1
            if (hi - lo >= 2) {
                int mid = (lo + hi) >>> 1;
                setPendingCount(1); // only one pending
                new ForEach(this, array, op, mid, hi).fork(); // right child
                new ForEach(this, array, op, lo, mid).compute(); // direct invoke
            } else {
                if (hi > lo) op.apply(array[lo]);
                tryComplete();
            }
        }
    }

    static class Searcher<E> extends CountedCompleter<E> {
        final E[] array; final AtomicReference<E> result; final int lo, hi;
        Searcher(CountedCompleter<?> p, E[] array, AtomicReference<E> result, int lo, int hi) {
            super(p);
            this.array = array; this.result = result; this.lo = lo; this.hi = hi;
        }
        public E getRawResult() { return result.get(); }
        public void compute() { // similar to ForEach version 3
            int l = lo,  h = hi;
            while (result.get() == null && h >= l) {
                if (h - l >= 2) {
                    int mid = (l + h) >>> 1;
                    addToPendingCount(1);
                    new Searcher(this, array, result, mid, h).fork();
                    h = mid;
                }
                else {
                    E x = array[l];
                    if (matches(x) && result.compareAndSet(null, x))
                        quietlyCompleteRoot(); // root task is now joinable
                    break;
                }
            }
            if (result.get() == null) {
                tryComplete(); // normally complete whether or not found
            }
        }
        boolean matches(E e) {
            return true;
        } // return true if found

        public static <E> E search(E[] array) {
            return new Searcher<E>(null, array, new AtomicReference<E>(), 0, array.length).invoke();
        }
    }

}
