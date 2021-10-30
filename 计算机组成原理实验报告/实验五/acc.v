module acc(clk,WR,in,out);
	input clk,WR;
	input[15:0]in;
	output[15:0]out;
	
	reg[15:0]date;
	initial begin
		date<=0;
	end
	
	assign out=date;
	
	always@(negedge clk)
	begin
		if(WR)date<=in;
	end
	

endmodule