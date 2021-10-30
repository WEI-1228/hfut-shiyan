module register(WR,clk,addr,in,out);//数据寄存器
	input WR,clk;
	input[11:0]addr;
	input[15:0]in;
	output[15:0]out;
	
	reg[15:0]units[12'b111111111111:0];
	
	assign out=units[addr];
	
	initial begin
		units[0]=16'b0000000000000001;
		units[1]=16'b1000000000000000;
	end
	
	always@(negedge clk)
	begin
		if(WR)
			units[addr]=in;
	end
	
	
	
endmodule