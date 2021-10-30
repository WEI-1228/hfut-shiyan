module D74ls138(
	input wire[2:0] in,
	output reg[7:0] out
);
	integer i;
	always@(*)
		begin
			for(i = 0;i<8;i=i+1)
				if(i==in)out[i]<=1;
				else out[i]<=0;
		end
endmodule
