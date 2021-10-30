module FourBitCmp_tb;

wire bigger,equal,smaller;
reg[4:1]ina,inb;

FourBitCmp uut(
.bigger(bigger),.equal(equal),.smaller(smaller),.ina(ina),.inb(inb)
);

initial begin

	ina = 4'b0111;
	inb = 4'b1001;
	#10;
	
	ina = 4'b1111;
	inb = 4'b1111;
	#10;
	
	ina = 4'b1100;
	inb = 4'b1001;
	#10;
	
	ina = 4'b0110;
	inb = 4'b1011;
	
	#10 $stop;


end

endmodule