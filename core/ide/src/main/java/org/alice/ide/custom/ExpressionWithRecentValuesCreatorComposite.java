/*******************************************************************************
 * Copyright (c) 2006, 2024, Carnegie Mellon University. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may
 *    "Alice" appear in their name, without prior written permission of
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is
 *    contributed by Electronic Arts Inc. and may be used for personal,
 *    non-commercial, and academic use only. Redistributions of any program
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/
package org.alice.ide.custom;

import org.alice.ide.custom.components.CustomExpressionCreatorView;

import org.lgna.croquet.CascadeBlankChild;
import org.lgna.croquet.CascadeLineSeparator;

import java.util.*;

/**
 * @author Dmitry Portnoy
 */
public abstract class ExpressionWithRecentValuesCreatorComposite<V extends CustomExpressionCreatorView, T> extends CustomExpressionCreatorComposite<V> {
    private static final int MAX_RECENT = 3;

    protected final LinkedList<T> recentValues = new LinkedList<>();

    public ExpressionWithRecentValuesCreatorComposite(UUID id) {
        super(id);
    }

    @Override
    protected void handlePostHideDialog() {
        super.handlePostHideDialog();

        if (isCommitted) {
            updateRecentValues();
        }
    }

    protected abstract T getLastCustomValue();

    protected abstract CascadeBlankChild getValueFillIn(T value);

    protected void updateRecentValues() {
        T customValue = getLastCustomValue();

        // if the element already exists, remove and then re-add it, effectively moving it to the front
        recentValues.remove(customValue);

        recentValues.add(customValue);
    }

    public List<CascadeBlankChild> getRecentFillIns(List<T> literals) {
        if (recentValues.isEmpty()) {
            return new ArrayList<>();
        }

        List<CascadeBlankChild> recentFillIns = new ArrayList<>(recentValues.size() + 1);

        Iterator<T> iter = recentValues.descendingIterator();

        while (iter.hasNext() && recentFillIns.size() < MAX_RECENT) {
            T value = iter.next();

            if (!literals.contains(value)) {
                recentFillIns.add(getValueFillIn(value));
            }
        }

        // add a separator between the hard-coded and recent values,
        // and reverse the list to show most revent values last
        if (!recentFillIns.isEmpty()) {
            recentFillIns.add(CascadeLineSeparator.getInstance());

            Collections.reverse(recentFillIns);
        }

        return recentFillIns;
    }
}
