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
package org.chocosolver.solver.objective;

import org.chocosolver.sat.PropSat;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.constraints.Operator;
import org.chocosolver.solver.search.loop.monitors.IMonitorSolution;
import org.chocosolver.solver.variables.IntVar;

import java.util.LinkedList;
import java.util.List;

/**
 * Class to store the pareto front (multi-objective optimization).
 * Worse solutions are dynamically removed from the solution set.
 *
 * @author Jean-Guillaume Fages
 */
public class ParetoOptimizer implements IMonitorSolution {

    //***********************************************************************************
    // VARIABLES
    //***********************************************************************************

    // Set of incomparable and Pareto-best solutions
    private LinkedList<Solution> paretoFront;

    private Model model;

    // Allow to recycle (dominated) Solution objects
    private LinkedList<Solution> pool = new LinkedList<>();

    // objective function
    private boolean maximize;
    private IntVar[] objectives;
    private int n;

    // to post dynamical constraints
    private int[] vals, lits;
    private PropSat psat;

    //***********************************************************************************
    // CONSTRUCTOR
    //***********************************************************************************

	/**
     * Create an object to compute the Pareto front of a multi-objective problem.
     * Maintain the set of dominating solutions and
     * posts constraints dynamically to prevent search from computing dominated ones.
     * This object must be used as follows:
     *
   	 *     model.getSolver().plugMonitor(paretoRecorder);
     *     while(model.getSolver().solve());
     *     List<Solution> paretoFront = paretoRecorder.getParetoFront();
     *
     * The Solutions store decision variables (those declared in the search strategy)
	 * BEWARE: requires the objectives to be declared in the search strategy
     *
     * @param maximize whether to maximize or minimize the objectives
     * @param objectives objective variables (must all be optimized in the same direction)
     */
    public ParetoOptimizer(final boolean maximize, final IntVar[] objectives) {
        this.paretoFront = new LinkedList<>();
        this.objectives = objectives.clone();
        this.maximize = maximize;
        n = objectives.length;
        model = objectives[0].getModel();
        psat = model.getMinisat().getPropSat();
        vals = new int[n];
        lits = new int[n];
    }

    //***********************************************************************************
    // METHODS
    //***********************************************************************************

    @Override
    public void onSolution() {
        // get objective values
        for (int i = 0; i < n; i++) {
            vals[i] = objectives[i].getValue();
        }
        // remove dominated solutions
        for (int i = paretoFront.size() - 1; i >= 0; i--) {
            if (isDominated(paretoFront.get(i), vals)) {
                pool.add(paretoFront.remove(i));
            }
        }
        // store current solution
        if(pool.isEmpty()){
            paretoFront.add(new Solution(model).record());
        }else{
            Solution solution = pool.remove();

            solution.record();
            paretoFront.add(solution);
        }
        // post dynamical constraints to prevent search from computing dominated solutions
        Operator symbol = Operator.GT;
        if (!maximize) {
            symbol = Operator.LT;
        }
        for (int i = 0; i < n; i++) {
            lits[i] = psat.makeLiteral(model.arithm(objectives[i], symbol.toString(), vals[i]).reify(), true);
        }
        psat.addLearnt(lits);
    }

	/**
     * @return the set of Pareto-best (possibly optimal) solutions found so far
     */
    public List<Solution> getParetoFront() {
   		return paretoFront;
   	}

    private boolean isDominated(Solution solution, int[] vals) {
        for (int i = 0; i < n; i++) {
            int delta = solution.getIntVal(objectives[i]) - vals[i];
            if ((delta > 0 && maximize) || (delta < 0 && !maximize)) {
                return false;
            }
        }
        return true;
    }
}
