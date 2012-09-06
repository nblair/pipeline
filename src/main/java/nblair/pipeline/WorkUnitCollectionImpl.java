/**
 *    Copyright 2012 Nicholas Blair
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package nblair.pipeline;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author Nicholas Blair
 */
public class WorkUnitCollectionImpl<E> implements WorkUnitCollection<E> {
	private final Collection<E> delegate;
	private volatile boolean _complete = false;
	/**
	 * @param delegate
	 */
	public WorkUnitCollectionImpl(Collection<E> delegate) {
		this.delegate = delegate;
	}
	/**
	 * @return
	 * @see java.util.Collection#size()
	 */
	public int size() {
		return delegate.size();
	}
	/**
	 * @return
	 * @see java.util.Collection#isEmpty()
	 */
	public boolean isEmpty() {
		return delegate.isEmpty();
	}
	/**
	 * @param o
	 * @return
	 * @see java.util.Collection#contains(java.lang.Object)
	 */
	public boolean contains(Object o) {
		return delegate.contains(o);
	}
	/**
	 * @return
	 * @see java.util.Collection#iterator()
	 */
	public Iterator<E> iterator() {
		return delegate.iterator();
	}
	/**
	 * @return
	 * @see java.util.Collection#toArray()
	 */
	public Object[] toArray() {
		return delegate.toArray();
	}
	/**
	 * @param a
	 * @return
	 * @see java.util.Collection#toArray(T[])
	 */
	public <T> T[] toArray(T[] a) {
		return delegate.toArray(a);
	}
	/**
	 * @param e
	 * @return
	 * @see java.util.Collection#add(java.lang.Object)
	 */
	public boolean add(E e) {
		return delegate.add(e);
	}
	/**
	 * @param o
	 * @return
	 * @see java.util.Collection#remove(java.lang.Object)
	 */
	public boolean remove(Object o) {
		return delegate.remove(o);
	}
	/**
	 * @param c
	 * @return
	 * @see java.util.Collection#containsAll(java.util.Collection)
	 */
	public boolean containsAll(Collection<?> c) {
		return delegate.containsAll(c);
	}
	/**
	 * @param c
	 * @return
	 * @see java.util.Collection#addAll(java.util.Collection)
	 */
	public boolean addAll(Collection<? extends E> c) {
		return delegate.addAll(c);
	}
	/**
	 * @param c
	 * @return
	 * @see java.util.Collection#removeAll(java.util.Collection)
	 */
	public boolean removeAll(Collection<?> c) {
		return delegate.removeAll(c);
	}
	/**
	 * @param c
	 * @return
	 * @see java.util.Collection#retainAll(java.util.Collection)
	 */
	public boolean retainAll(Collection<?> c) {
		return delegate.retainAll(c);
	}
	/**
	 * 
	 * @see java.util.Collection#clear()
	 */
	public void clear() {
		delegate.clear();
	}
	/**
	 * @param o
	 * @return
	 * @see java.util.Collection#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		return delegate.equals(o);
	}
	/**
	 * @return
	 * @see java.util.Collection#hashCode()
	 */
	public int hashCode() {
		return delegate.hashCode();
	}
	@Override
	public boolean isComplete() {
		return _complete;
	}
	@Override
	public void signalCompletion() {
		_complete = true;
	}

}
