package pingpong.jdt.core.classes;

import org.eclipse.jdt.internal.compiler.flow.FlowInfo;
import org.eclipse.jdt.internal.compiler.flow.UnconditionalFlowInfo;
import org.eclipse.jdt.internal.compiler.lookup.FieldBinding;
import org.eclipse.jdt.internal.compiler.lookup.LocalVariableBinding;

@SuppressWarnings("restriction")
public class FakeFlowInfo extends FlowInfo {

	private final FlowInfo exitBranch;
	private final FlowInfo actionInfo_unconditionalInits;

	public FakeFlowInfo(FlowInfo exitBranch, FlowInfo actionInfo_unconditionalInits) {
		this.exitBranch = exitBranch;
		this.actionInfo_unconditionalInits = actionInfo_unconditionalInits;
	}

	@Override
	public FlowInfo addInitializationsFrom(FlowInfo condInfo_initsWhenFalse) {
		// reverse calling order of addInitializationsFrom() and
		// addPotentialInitializationsFrom()
		return exitBranch.addInitializationsFrom(condInfo_initsWhenFalse).addPotentialInitializationsFrom(actionInfo_unconditionalInits);
	}

	@Override
	public FlowInfo addPotentialInitializationsFrom(FlowInfo otherInits) {
		throw new RuntimeException("Unexpected call");
	}

	@Override
	public FlowInfo copy() {
		throw new RuntimeException("Unexpected call");
	}

	@Override
	public FlowInfo initsWhenFalse() {
		throw new RuntimeException("Unexpected call");
	}

	@Override
	public FlowInfo initsWhenTrue() {
		throw new RuntimeException("Unexpected call");
	}

	@Override
	public boolean isDefinitelyAssigned(FieldBinding field) {
		throw new RuntimeException("Unexpected call");
	}

	@Override
	public boolean isDefinitelyAssigned(LocalVariableBinding local) {
		throw new RuntimeException("Unexpected call");
	}

	@Override
	public boolean isDefinitelyNonNull(LocalVariableBinding local) {
		throw new RuntimeException("Unexpected call");
	}

	@Override
	public boolean isDefinitelyNull(LocalVariableBinding local) {
		throw new RuntimeException("Unexpected call");
	}

	@Override
	public boolean isDefinitelyUnknown(LocalVariableBinding local) {
		throw new RuntimeException("Unexpected call");
	}

	@Override
	public boolean isPotentiallyAssigned(FieldBinding field) {
		throw new RuntimeException("Unexpected call");
	}

	@Override
	public boolean isPotentiallyAssigned(LocalVariableBinding field) {
		throw new RuntimeException("Unexpected call");
	}

	@Override
	public boolean isPotentiallyNonNull(LocalVariableBinding local) {
		throw new RuntimeException("Unexpected call");
	}

	@Override
	public boolean isPotentiallyNull(LocalVariableBinding local) {
		throw new RuntimeException("Unexpected call");
	}

	@Override
	public boolean isPotentiallyUnknown(LocalVariableBinding local) {
		throw new RuntimeException("Unexpected call");
	}

	@Override
	public boolean isProtectedNonNull(LocalVariableBinding local) {
		throw new RuntimeException("Unexpected call");
	}

	@Override
	public boolean isProtectedNull(LocalVariableBinding local) {
		throw new RuntimeException("Unexpected call");
	}

	@Override
	public void markAsComparedEqualToNonNull(LocalVariableBinding local) {
		throw new RuntimeException("Unexpected call");
	}

	@Override
	public void markAsComparedEqualToNull(LocalVariableBinding local) {
		throw new RuntimeException("Unexpected call");
	}

	@Override
	public void markAsDefinitelyAssigned(FieldBinding field) {
		throw new RuntimeException("Unexpected call");
	}

	@Override
	public void markAsDefinitelyAssigned(LocalVariableBinding local) {
		throw new RuntimeException("Unexpected call");
	}

	@Override
	public void markAsDefinitelyNonNull(LocalVariableBinding local) {
		throw new RuntimeException("Unexpected call");
	}

	@Override
	public void markAsDefinitelyNull(LocalVariableBinding local) {
		throw new RuntimeException("Unexpected call");
	}

	@Override
	public void markAsDefinitelyUnknown(LocalVariableBinding local) {
		throw new RuntimeException("Unexpected call");
	}

	@Override
	public UnconditionalFlowInfo mergedWith(UnconditionalFlowInfo otherInits) {
		throw new RuntimeException("Unexpected call");
	}

	@Override
	public UnconditionalFlowInfo nullInfoLessUnconditionalCopy() {
		throw new RuntimeException("Unexpected call");
	}

	@Override
	public FlowInfo safeInitsWhenTrue() {
		throw new RuntimeException("Unexpected call");
	}

	@Override
	public FlowInfo setReachMode(int reachMode) {
		throw new RuntimeException("Unexpected call");
	}

	@Override
	public UnconditionalFlowInfo unconditionalCopy() {
		throw new RuntimeException("Unexpected call");
	}

	@Override
	public UnconditionalFlowInfo unconditionalFieldLessCopy() {
		throw new RuntimeException("Unexpected call");
	}

	@Override
	public UnconditionalFlowInfo unconditionalInits() {
		throw new RuntimeException("Unexpected call");
	}

	@Override
	public UnconditionalFlowInfo unconditionalInitsWithoutSideEffect() {
		throw new RuntimeException("Unexpected call");
	}

}
