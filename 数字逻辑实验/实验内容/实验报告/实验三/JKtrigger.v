module JKtrigger(J,K,clk,Q,set,reset);
	input J,K,clk,set,reset;
	output Q;
	reg Q;
	always@(negedge clk)
	begin
		if(!set)Q <= 1;
		else if(!reset)Q <= 0;
		else
		begin
			case({J,K})
				2'b00:Q <= Q;
				2'b01:Q <= 0;
				2'b10:Q <= 1;
				default:Q<=~Q;
			endcase
		end
	end
endmodule