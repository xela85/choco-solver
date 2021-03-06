/**
 * Copyright (c) 2016, Ecole des Mines de Nantes
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    This product includes software developed by the <organization>.
 * 4. Neither the name of the <organization> nor the
 *    names of its contributors may be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY <COPYRIGHT HOLDER> ''AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.chocosolver.util.objects.setDataStructures.bitset;

import org.chocosolver.util.objects.setDataStructures.ISet;
import org.chocosolver.util.objects.setDataStructures.ISetIterator;
import org.chocosolver.util.objects.setDataStructures.SetType;

import java.util.BitSet;
import java.util.Iterator;

/**
 * BitSet implementation for a set of integers
 * Supports negative numbers when using int... constructor
 *
 * @author Jean-Guillaume Fages, Xavier Lorca
 */
public class Set_BitSet implements ISet {

	//***********************************************************************************
	// VARIABLES
	//***********************************************************************************

	protected int card;
	protected int offset;  // allow using negative numbers
	protected BitSet values = new BitSet();
	private ISetIterator iter = newIterator();

	//***********************************************************************************
	// ITERATOR
	//***********************************************************************************

	@Override
	public Iterator<Integer> iterator(){
		iter.reset();
		return iter;
	}

	@Override
	public ISetIterator newIterator(){
		return new ISetIterator() {
			private int current = -1;
			@Override
			public void reset() {
				current = -1;
			}
			@Override
			public boolean hasNext() {
				return values.nextSetBit(current+1) >= 0;
			}
			@Override
			public Integer next() {
				current = values.nextSetBit(current + 1);
				return current+offset;
			}
		};
	}

	//***********************************************************************************
	// CONSTRUCTOR
	//***********************************************************************************

	/**
	 * Creates an empty bitset having numbers greater or equal than <code>offSet</code> (possibly < 0)
	 *
	 * @param offSet minimum value in the set
	 */
	public Set_BitSet(int offSet) {
		super();
		card = 0;
		offset = offSet;
	}

	//***********************************************************************************
	// METHODS
	//***********************************************************************************

	@Override
	public boolean add(int element) {
		if(element < offset) throw new IllegalStateException("Cannot add "+element+" to set of offset "+offset);
		if (values.get(element-offset)) {
			return false;
		}else{
			card++;
			values.set(element-offset);
			return true;
		}
	}

	@Override
	public boolean remove(int element) {
		if(contains(element)) {
			values.clear(element - offset);
			card--;
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean contains(int element) {
		return element >= offset && values.get(element - offset);
	}

	@Override
	public int size() {
		return card;
	}

	@Override
	public void clear() {
		card = 0;
		values.clear();
	}

	@Override
	public int min() {
		if(isEmpty()) throw new IllegalStateException("cannot find minimum of an empty set");
		return offset+values.nextSetBit(0);
	}

	@Override
	public int max() {
		if(isEmpty()) throw new IllegalStateException("cannot find maximum of an empty set");
		return offset+values.previousSetBit(values.length());
	}

	@Override
	public SetType getSetType(){
		return SetType.BITSET;
	}

	@Override
	public String toString() {
		String st = "{";
		for(int i:this){
			st+=i+", ";
		}
		st+="}";
		return st.replace(", }","}");
	}
}
