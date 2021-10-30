module threeDoor_tb;

reg cin,EN;
wire cout;

initial begin
cin = 1'b1;
EN = 1'b0;
#10 EN = 1'b1;

#10 EN = 1'b0;

#10 EN = 1'b1;

#10 EN = 1'b0;

end
threeDoor uut(
.cin(cin), .cout(cout), .EN(EN)
);
endmodule