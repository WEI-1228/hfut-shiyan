module CU(ins,WE,op);
	input[6:0]ins;
	output reg WE;
	output reg[2:0]op;
	
	always@(*)
	begin
		if(ins==0)WE<=1;
		op<=3'b000;
	end

endmodule