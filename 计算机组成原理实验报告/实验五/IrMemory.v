module IrMemory(addr,ins);
	input[11:0]addr;
	output reg[15:0]ins;
	
	reg[15:0]units[12'b111111111111:0];
	
	initial begin
		units[0]<=16'b0111_0000_0000_0000;//LDA 0
		units[1]<=16'b0001_0000_0000_0000;//COM
		units[2]<=16'b0010_0000_0000_0000;//SHR
		units[3]<=16'b0011_0000_0000_0000;//CSL
		units[4]<=16'b0000_0000_0000_0000;//CLA
		units[5]<=16'b0101_0000_0000_0001;//ADD
		units[6]<=16'b0110_0000_0000_0010;//STA
		units[7]<=16'b1000_0000_0000_1001;//JMP 9
		units[9]<=16'b1001_0000_0000_0010;//BAN pc+2
		units[11]<=16'b0100_0000_0000_0000;//STP
	end
	
	always@(*)
	begin
		ins=units[addr];
	end
endmodule