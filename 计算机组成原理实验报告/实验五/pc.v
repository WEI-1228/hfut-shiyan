module pc(clk,rst,addr,stp,WR,imm,ban);
	input clk,rst,stp,WR,ban;
	input [11:0]imm;
	output reg[11:0]addr;
	
	reg state;
	initial begin
		state=1;
		addr=0;
	end
	
	always@(*)
	begin
		if(stp)
			state=0;
		if(rst)
			addr=0;		
	end
	
	always@(posedge clk)
	begin
		if(state)
			addr=addr+1;
	end
	
	always@(negedge clk)
	begin
		if(WR)
			addr=imm-1;
		if(ban)
			addr=addr+imm-1;
	end

endmodule