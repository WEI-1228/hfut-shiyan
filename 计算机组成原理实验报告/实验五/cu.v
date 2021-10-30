module cu(ins,op,stp,pcWR,accWR,dateWR);
	input[3:0]ins;
	output reg [3:0]op;
	output reg stp,pcWR,accWR,dateWR;
	
	initial begin
		accWR<=0;
		dateWR<=0;
		pcWR<=0;
		stp<=0;
		op<=4'b0100;
	end
	
	always@(ins)
	begin
		case(ins)
			4'b0000:{stp,pcWR,accWR,dateWR,op}=8'b00100000;//CLA
			4'b0001:{stp,pcWR,accWR,dateWR,op}=8'b00100001;//COM
			4'b0010:{stp,pcWR,accWR,dateWR,op}=8'b00100010;//SHR
			4'b0011:{stp,pcWR,accWR,dateWR,op}=8'b00100011;//CSL
			4'b0100:{stp,pcWR,accWR,dateWR,op}=8'b10000100;//STP
			4'b0101:{stp,pcWR,accWR,dateWR,op}=8'b00100101;//ADD
			4'b0110:{stp,pcWR,accWR,dateWR,op}=8'b00010110;//STA
			4'b0111:{stp,pcWR,accWR,dateWR,op}=8'b00100111;//LDA
			4'b1000:{stp,pcWR,accWR,dateWR,op}=8'b01001000;//JMP
			4'b1001:{stp,pcWR,accWR,dateWR,op}=8'b00001001;//BAN
		endcase
	end

endmodule