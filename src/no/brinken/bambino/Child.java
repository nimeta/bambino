package no.brinken.bambino;

public class Child extends TableObject
{
	@IdField
	@PrimaryKey
	public long child_id;
	public String first_name;
	public String last_name;
	public String birth_date;
	public String birth_time;
	public String identity_no;
}
