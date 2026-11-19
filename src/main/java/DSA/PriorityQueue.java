/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DSA;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author rownavanana
 */
public class PriorityQueue<T> {

    private final ArrayList<T> heap = new ArrayList<>();
    private final Comparator<? super T> comparator;

    public PriorityQueue(Comparator<? super T> comparator) {
        this.comparator = comparator;
    }

    public void enqueue(T item) {
        heap.add(item);
        siftUp(heap.size() - 1);
    }

    public T dequeue() {
        if (heap.isEmpty()) {
            return null;
        }
        T top = heap.get(0);
        T last = heap.remove(heap.size() - 1);
        if (!heap.isEmpty()) {
            heap.set(0, last);
            siftDown(0);
        }
        return top;
    }

    public T peek() {
        return heap.isEmpty() ? null : heap.get(0);
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    public int size() {
        return heap.size();
    }

    public void clear() {
        heap.clear();
    }

    public List<T> toSortedList() {
        PriorityQueue<T> copy = new PriorityQueue<T>(comparator);
        copy.heap.addAll(heap);
        List<T> sorted = new ArrayList<>();
        while (!copy.isEmpty()) {
            sorted.add(copy.dequeue());
        }
        return sorted;
    }

    public static <T> List<T> sort(Collection<T> items, Comparator<? super T> comparator) {
        PriorityQueue<T> queue = new PriorityQueue<T>(comparator);
        for (T item : items) {
            queue.enqueue(item);
        }
        return queue.toSortedList();
    }

    private void siftUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (comparator.compare(heap.get(index), heap.get(parent)) >= 0) {
                break;
            }
            swap(index, parent);
            index = parent;
        }
    }

    private void siftDown(int index) {
        int size = heap.size();
        while (true) {
            int left = 2 * index + 1;
            int right = left + 1;
            int smallest = index;
            if (left < size && comparator.compare(heap.get(left), heap.get(smallest)) < 0) {
                smallest = left;
            }
            if (right < size && comparator.compare(heap.get(right), heap.get(smallest)) < 0) {
                smallest = right;
            }
            if (smallest == index) {
                break;
            }
            swap(index, smallest);
            index = smallest;
        }
    }

    private void swap(int a, int b) {
        T temp = heap.get(a);
        heap.set(a, heap.get(b));
        heap.set(b, temp);
    }
}
