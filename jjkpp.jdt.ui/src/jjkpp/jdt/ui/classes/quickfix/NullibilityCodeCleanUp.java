/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package jjkpp.jdt.ui.classes.quickfix;

import jjkpp.jdt.core.classes.NullibilityAnnos;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.internal.ui.fix.AbstractMultiFix;
import org.eclipse.jdt.ui.cleanup.CleanUpRequirements;
import org.eclipse.jdt.ui.cleanup.ICleanUpFix;
import org.eclipse.jdt.ui.text.java.IProblemLocation;

@SuppressWarnings("restriction")
public class NullibilityCodeCleanUp extends AbstractMultiFix {

	// TO ENABLE QUICKFIX, ADD FOLLOWING TO PLUGIN.XML of JDT.UI
	// <cleanUp
	// class="org.eclipse.jdt.internal.ui.fix.NullibilityCodeCleanUp"
	// id="org.eclipse.jdt.ui.cleanup.nullibility_code"
	// runAfter="org.eclipse.jdt.ui.cleanup.unimplemented_code">
	// </cleanUp>
	// <cleanUp
	// class="org.eclipse.jdt.internal.ui.fix.SortMembersCleanUp"
	// ...

	public static final String MAKE_TYPE_ABSTRACT = "cleanup.make_type_abstract_if_missing_method"; //$NON-NLS-1$

	public NullibilityCodeCleanUp() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	public String[] getStepDescriptions() {
		return new String[] { "Add nullibility annotations" };
	}

	/**
	 * {@inheritDoc}
	 */
	public String getPreview() {
		StringBuffer buf = new StringBuffer();
		return buf.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	public CleanUpRequirements getRequirements() {
		return new CleanUpRequirements(true, false, false, null);
	}

	/**
	 * {@inheritDoc}
	 */
	protected ICleanUpFix createFix(CompilationUnit unit) throws CoreException {
		IProblemLocation[] problemLocations = convertProblems(unit.getProblems());
		problemLocations = filter(problemLocations, new int[] { NullibilityAnnos.RequireCanBeNull });

		return NullibilityCodeFix.createCleanUp(unit, problemLocations);
	}

	/**
	 * {@inheritDoc}
	 */
	protected ICleanUpFix createFix(CompilationUnit unit, IProblemLocation[] problems) throws CoreException {
		IProblemLocation[] problemLocations = filter(problems, new int[] { NullibilityAnnos.RequireCanBeNull });
		return NullibilityCodeFix.createCleanUp(unit, problemLocations);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean canFix(ICompilationUnit compilationUnit, IProblemLocation problem) {
		int id = problem.getProblemId();
		if (id == NullibilityAnnos.RequireCanBeNull)
			return true;

		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public int computeNumberOfFixes(CompilationUnit compilationUnit) {

		IProblemLocation[] locations = filter(convertProblems(compilationUnit.getProblems()), new int[] { NullibilityAnnos.RequireCanBeNull });

		return locations.length;
	}

}
