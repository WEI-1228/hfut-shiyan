module FourAdd(ina,inb,co,sum,cout);
	input[4:1]ina,inb;
	input co;
	output[4:1] sum;
	output cout;
	assign {cout,sum} = ina+inb+co;
endmodule
