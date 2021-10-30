module cpu(clk,rst);
	input clk,rst;
	
	wire stp,pcWR,accWR,dateWR,ban;
	wire[3:0]op;
	wire[11:0]addr;
	wire[15:0]ins,A,B,out;
	
	pc pc(
		.clk(clk),.rst(rst),.stp(stp),.addr(addr),.ban(ban),.WR(pcWR),.imm(ins[11:0])
	);
	
	IrMemory IrMemory(
		.addr(addr),.ins(ins)
	);
	
	acc acc(
		.clk(clk),.WR(accWR),.in(out),.out(A)
	);
	
	register register(
		.WR(dateWR),.clk(clk),.addr(ins[11:0]),.in(out),.out(B)
	);
	
	cu cu(
		.ins(ins[15:12]),.op(op),.stp(stp),.pcWR(pcWR),.accWR(accWR),.dateWR(dateWR)
	);
	
	alu alu(
		.op(op),.A(A),.B(B),.ban(ban),.out(out)
	);
	
	
endmodule