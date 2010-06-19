package pingpong.jdt.test;

import org.eclipse.jdt.internal.compiler.flow.FlowInfo;
import org.eclipse.jdt.internal.compiler.flow.UnconditionalFlowInfo;

public enum NullBitsCombi {
	
	/*
			nullBit1
			 nullBit2...
			0000	start
			0001	pot. unknown
			0010	pot. non null
			0011	pot. nn & pot. un
			0100	pot. null
			0101	pot. n & pot. un
			0110	pot. n & pot. nn
			1001	def. unknown
			1010	def. non null
			1011	pot. nn & prot. nn
			1100	def. null
			1101	pot. n & prot. n
			1110	prot. null
			1111	prot. non null
	 */

	
	Start("0000","start"),
	PotUnknown("0001","pot. unknown"),		
	PotNonnull("0010","pot. non null"),
	PotNn_PotUn("0011","pot. nn & pot. un"),
	PotNull("0100","pot. null"),
	PotN_PotUn("0101","pot. n & pot. un"),
	PotN_PotNn("0110","pot. n & pot. nn"),
	DefUnknown("1001","def. unknown"),
	DefNonnull("1010","def. non null"),
	PotNn_ProtNn("1011","pot. nn & prot. nn"),
	DefNull("1100","def. null"),
	PotN_ProtN("1101","pot. n & prot. n"),
	ProtNull("1110","prot. null"),
	ProtNonnull("1111","prot. non null");
	
	private final String d;
	private final String desc;

	@Override
	public String toString() {
		String result=desc;
		while (result.length()<20)
			result+=" ";
//		TestNullBits a=newBits();
//		result+=""+a.nullBit1+""+a.nullBit2+""+a.nullBit3+""+a.nullBit4;
		return result;
	}

	private NullBitsCombi(String d, String desc) {
		this.d = d;
		this.desc = desc;			
	}
	
	UnconditionalFlowInfo bits() {
		UnconditionalFlowInfo result=new UnconditionalFlowInfo();
		int b = Integer.parseInt(d, 2);
		result.nullBit1=b / 8 % 2;
		result.nullBit2=b / 4 % 2;
		result.nullBit3=b / 2 % 2;
		result.nullBit4=b / 1 % 2;
		result.tagBits |= FlowInfo.NULL_FLAG_MASK;
		return result;
	}

	public static Object find(UnconditionalFlowInfo a) {
		for (NullBitsCombi combi:NullBitsCombi.values()) {	
			UnconditionalFlowInfo bits = combi.bits();
			if (bits.nullBit1==a.nullBit1 && bits.nullBit2==a.nullBit2 && bits.nullBit3==a.nullBit3 && bits.nullBit4==a.nullBit4)
				return combi;			
		}
		return ""+a.nullBit1+""+a.nullBit2+""+a.nullBit3+""+a.nullBit4;
	}
}
