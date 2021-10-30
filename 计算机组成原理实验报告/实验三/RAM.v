module RAM(CLK,WE,wval,addr,wout);
	input CLK,WE;
	input[9:1]addr;
	input[16:1]wval;
	output[16:1]wout;
	
	reg[16:1]regHeap[9'b1000000000:1];
	
	assign wout=regHeap[addr];
	
	always@(posedge CLK)
	begin
		if(!WE)regHeap[addr]<=wval;
	end

endmodule