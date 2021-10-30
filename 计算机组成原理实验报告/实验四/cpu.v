module cpu(clk,rst);
	input clk;
	input rst;
	
	wire WE;
	wire[7:0]addr;
	wire[2:0]op;
	wire[15:0]ins,r1,r2,w_data;
	
	PC pc(
		.clk(clk),.rst(rst),.pc(addr)
	);
	

	IrMemory mem(
		.addr(addr),.ins(ins)
	);
	
	CU cu(
		.ins(ins[15:9]),.WE(WE),.op(op)
	);
	
	ALU alu(
		.A(r1),.B(r2),.op(op),.out(w_data)
	);
	
	register regs(
		.r1(ins[8:6]),.r2(ins[5:3]),.WE(WE),.w1(ins[2:0]),.wval(w_data),.out1(r1),.out2(r2),.CLK(clk)
	);
	
	
endmodule