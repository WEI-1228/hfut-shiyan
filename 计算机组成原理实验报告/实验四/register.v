module register(r1,r2,WE,w1,wval,out1,out2,CLK);
	input wire CLK,WE;
	input[2:0]r1,r2,w1;
	input[15:0]wval;
	output reg[15:0]out1,out2;
	reg[15:0]regHeap[7:0];
	integer i;
	
	initial begin
		for(i=0;i<8;i=i+1)
		begin
			regHeap[i]=i;
		end
	end
	
	
	always@(*)
	begin	
		out1<=regHeap[r1];
		out2<=regHeap[r2];
	end
	
	always@(negedge CLK)
		begin
			if(WE)regHeap[w1]<=wval;
		end
endmodule