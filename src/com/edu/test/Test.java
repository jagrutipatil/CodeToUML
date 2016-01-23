package com.edu.test;

public class Test {
	
	private int[] list;
	private int rear;
	private int front;
	private int size;
	private int capacity;
	
	void enqueue(int no) {
		if (size == capacity) {
			System.out.printf("Queue is full");
		}
		
		list[rear++] = no;
		size ++;
	}	
	
	int deque() {
		if (size == 0) {
			System.out.printf("Queue us empty");
			front = 0;
			rear = 0;
		}
		size--;
		int ret = list[front++]; 
		return ret;
	}
	

}
