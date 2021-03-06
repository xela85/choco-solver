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
package org.chocosolver.solver.search.strategy.decision;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.search.strategy.assignments.DecisionOperator;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * <p>
 * Project: choco-solver.
 *
 * @author Charles Prud'homme
 * @since 14/03/2016.
 */
public class DecisionMakerTest {

    Model model;
    DecisionMaker dm;

    @BeforeMethod(alwaysRun = true)
    public void setUp(){
        model = new Model();
        dm = new DecisionMaker();
    }


    @Test(groups = "1s", timeOut=60000)
    public void testMakeIntDecision() throws Exception {
        IntDecision d = dm.makeIntDecision(model.boolVar(), DecisionOperator.int_eq, 0);
        d.free();
        d = dm.makeIntDecision(model.boolVar(), DecisionOperator.int_eq, 1);
        d.free();
    }

    @Test(groups = "1s", timeOut=60000)
    public void testMakeRealDecision() throws Exception {
        RealDecision d = dm.makeRealDecision(model.realVar(1d, 3d), 1.5);
        d.free();
        d = dm.makeRealDecision(model.realVar(2d, 4d), 2.5);
        d.free();
    }

    @Test(groups = "1s", timeOut=60000)
    public void testMakeSetDecision() throws Exception {
        SetDecision d = dm.makeSetDecision(model.setVar(new int[]{2,3,4}), DecisionOperator.set_force, 3);
        d.free();
        d = dm.makeSetDecision(model.setVar(new int[]{3,4, 5}), DecisionOperator.set_remove, 4);
        d.free();
    }
}