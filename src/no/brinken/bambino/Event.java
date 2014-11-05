package no.brinken.bambino;

import no.brinken.orm.PrimaryKey;
import no.brinken.orm.TableObject;


public class Event extends TableObject
{
	@PrimaryKey
	public long child_id;
	@PrimaryKey
	public String event_date;
	@PrimaryKey
	public long event_type_id;
}
