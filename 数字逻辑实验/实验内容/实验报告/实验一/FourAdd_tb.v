module FourAdd_tb;
reg[4:1]ina,inb;
reg co;
wire[4:1] sum;
wire cout;
initial begin
ina = 4'b1111;
inb = 4'b0001;
co = 1;
#10;

ina = 4'b0011;
inb = 4'b1100;
co = 0;
#10;
ina = 4'b1110;
inb = 4'b1011;
co = 1;
#10;
ina = 4'b1100;
inb = 4'b1111;
co = 1;

#10 $stop;
end

FourAdd uut(
.ina(ina),.inb(inb),.co(co),.sum(sum),.cout(cout)
);

endmodule