module threeDoor(
cin,
cout,
EN);
input cin;
input EN;
output cout;
reg cout;
always @(cin,EN)
begin
	if(EN)cout=cin;
	else cout = 'bz;
end
endmodule