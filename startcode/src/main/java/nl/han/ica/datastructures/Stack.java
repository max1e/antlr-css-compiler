package nl.han.ica.datastructures;

// Nog geen rekening gehouden met lege list
public class Stack <T> implements IHANStack<T> {

    private ListNode<T> top;

    @Override
    public void push(T value) {
        top = new ListNode<>(value, top);
    }

    @Override
    public T pop() {
        var topValue = top.getElement();
        top = top.getNext();
        return topValue;
    }

    @Override
    public T peek() {
        return top.getElement();
    }
}
