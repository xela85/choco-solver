/**
 *  Copyright (c) 1999-2011, Ecole des Mines de Nantes
 *  All rights reserved.
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *      * Redistributions of source code must retain the above copyright
 *        notice, this list of conditions and the following disclaimer.
 *      * Redistributions in binary form must reproduce the above copyright
 *        notice, this list of conditions and the following disclaimer in the
 *        documentation and/or other materials provided with the distribution.
 *      * Neither the name of the Ecole des Mines de Nantes nor the
 *        names of its contributors may be used to endorse or promote products
 *        derived from this software without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND ANY
 *  EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL THE REGENTS AND CONTRIBUTORS BE LIABLE FOR ANY
 *  DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package solver.search.strategy.enumerations.sorters.metrics;

import gnu.trove.THashSet;
import solver.constraints.Constraint;
import solver.constraints.propagators.Propagator;
import solver.variables.Variable;

import java.util.Arrays;

/**
 * A metric to evaluate wether or not a variable belongs to a constraint
 * <br/>
 *
 * @author Charles Prud'homme
 * @since 10/05/11
 */
public class Belong<V extends Variable> implements IMetric<V> {

    final THashSet<V> set;

    private Belong(Constraint<V, ? extends Propagator<V>> constraint) {
        this.set = new THashSet<V>();
        this.set.addAll(Arrays.asList(constraint.getVariables()));
    }

    public static <V extends Variable> Belong build(Constraint<V, ? extends Propagator<V>> constraint) {
        return new Belong<V>(constraint);
    }


    @Override
    public int eval(V var) {
        if (set.contains(var)) {
            return 0;
        }
        return 1;
    }
}