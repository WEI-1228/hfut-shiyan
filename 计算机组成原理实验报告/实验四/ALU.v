module ALU(A,B,op,out);
	input[16:1]A,B;
	input[3:1]op;
	output[16:1]out;
	reg[16:1]out;
	always@(*)
		begin
			case(op)
				3'b000:out<=A+B;
				3'b001:out<=A-B;
				3'b010:out<=A&&B;
				3'b011:out<=A||B;
				3'b100:out<=A<<B;
				3'b101:out<=A>>B;
			endcase
		end
endmodule