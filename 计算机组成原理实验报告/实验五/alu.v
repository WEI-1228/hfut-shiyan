module alu(A,B,op,out,ban);
	input[15:0]A,B;
	input[3:0]op;
	output reg[15:0]out;
	output reg ban;
	
	initial begin
		ban=0;
		out=0;
	end
	always@(*)
		begin
			case(op)
				4'b0000:out<=0;//CLA
				4'b0001:out<=~A;//COM
				4'b0010:out<={A[15],A[15:1]};//SHR
				4'b0011:out<={A[14:0],A[15]};//CSL
				4'b0100:;//STP
				4'b0101:out<=A+B;//ADD
				4'b0110:out<=A;//STA
				4'b0111:out<=B;//LDA
				4'b1000:;//JMP
				4'b1001:ban<=A[15];//BAN
			endcase
		end
endmodule