module Dtrigger(D,CLK,Q,reset,set);
	input D,CLK,set,reset;
	output Q;
	reg Q;
	always@(posedge CLK or negedge reset or negedge set)
	begin
		if(!set)Q <= 1;
		else if(!reset)Q <= 0;
		else Q <= D;
	end
endmodule