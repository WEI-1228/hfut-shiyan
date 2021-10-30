module register(r1,r2,WE,w1,wval,out1,out2,CLK);
	input wire CLK,WE;
	input[3:1]r1,r2,w1;
	input[16:1]wval;
	output[16:1]out1,out2;
	reg[16:1]regHeap[8:1];
	
	assign out1=regHeap[r1];
	assign out2=regHeap[r2];
	
	always@(posedge CLK)
		begin
			if(WE)regHeap[w1]<=wval;
		end
endmodule