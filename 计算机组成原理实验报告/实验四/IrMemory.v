module IrMemory(addr,ins);
	input[7:0]addr;
	output reg[15:0]ins;
	
	reg[15:0]units[8'b11111111:0];
	integer i;
	
	initial begin
		for(i=0;i<256;i=i+1)
		begin
			units[i][2:0]=1;
			units[i][5:3]=2;
			units[i][8:6]=3;
			units[i][15:9]=0;
		end
	end
	
	always@(*)
	begin
		ins=units[addr];
	end
	

endmodule