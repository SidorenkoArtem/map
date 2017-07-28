/* Javascript Calendar
(c) kdg http://HTMLWEB.RU/java/example/calendar_kdg.php */

var _Calendar=function(){
var _Calendar={
now:null,
sccd:null,
sccm:null,
sccy:null,
ccm:null,
ccy:null,
updobj:null,
mn:new Array('Январь','Февраль','Март','Апрель','Май','Июнь','Июль','Август','Сентрябрь','Октябрь','Ноябрь','Декабрь'),
mnn:new Array('31','28','31','30','31','30','31','31','30','31','30','31'),
mnl:new Array('31','29','31','30','31','30','31','31','30','31','30','31'),
calvalarr:new Array(42),

$:function(objID)
{
    if(document.getElementById){return document.getElementById(objID);}
    else if(document.all){return document.all[objID];}
    else if(document.layers){return document.layers[objID];}
},

checkClick:function(e){
	e?evt=e:evt=event;
	CSE=evt.target?evt.target:evt.srcElement;
	if (_Calendar.$('fc'))
		if (!_Calendar.isChild(CSE,_Calendar.$('fc')))/*  шапка календаря  только после нажатия*/
			_Calendar.$('fc').style.display='none';
			
		
},

isChild:function(s,d){
	while(s){
		if (s==d)
			return true;
		s=s.parentNode;
	}
	return false;
},

Left:function(obj){
	var curleft = 0;
	if (obj.offsetParent)
	{
		while (obj.offsetParent)
		{
			curleft += obj.offsetLeft
			obj = obj.offsetParent;
		}
	}
	else if (obj.x)
		curleft += obj.x;
	return curleft;
},

Top:function(obj){
	var curtop = 0;
	if (obj.offsetParent)
	{
		while (obj.offsetParent)
		{
			curtop += obj.offsetTop
			obj = obj.offsetParent;
		}
	}
	else if (obj.y)
		curtop += obj.y;
	return curtop;
},


lcs:function(ielem){/*сразу*/
    _Calendar.updobj=ielem;
    _Calendar.$('fc').style.left=_Calendar.Left(ielem)+'px';//календарь выпадает слева
    _Calendar.$('fc').style.top=_Calendar.Top(ielem)+ielem.offsetHeight+'px';
	_Calendar.$('fc').style.display='';
	_Calendar.$('fc').style.fontcolor='#2d6ca2';
			//_Calendar.$('fc').style.borderColor='#2d6ca2';
			//_Calendar.$('fc').style.borderStyle='solid';
			//_Calendar.$('fc').style.borderWidths='7 px';
			_Calendar.$('fc').style.zIndex='2';
			_Calendar.$('fc').style.backgroundColor='#e6e6e6';//фон шапки
	

	// First check date is valid
	curdt=ielem.value;
	curdtarr=curdt.split('-');
	isdt=true;
	for(var k=0;k<curdtarr.length;k++){
		if (isNaN(curdtarr[k]))
			isdt=false;
	}
	if (isdt&(curdtarr.length==3)){
		_Calendar.ccm=curdtarr[1]-1;
		_Calendar.ccy=curdtarr[2];
		_Calendar.prepcalendar(curdtarr[0],curdtarr[1]-1,curdtarr[2]);
	}

},

evtTgt:function(e)
{
	var el;
	if(e.target)el=e.target;
	else if(e.srcElement)el=e.srcElement;
	if(el.nodeType==3)el=el.parentNode; // defeat Safari bug
	return el;
},
EvtObj:function(e){if(!e)e=window.event;return e;},

cs_over:function(e){/* меняет цвет того квадратика календаря к какому  подношу мышку*/
	_Calendar.evtTgt(_Calendar.EvtObj(e)).style.background='#8DB0E8';
},

cs_out:function(e){/* меняет цвет квадратиков календаря когда после того как подношу мышку  и оставляет такими*/
	_Calendar.evtTgt(_Calendar.EvtObj(e)).style.background='#FFFFFF';
},

cs_click:function(e){
	_Calendar.updobj.value=_Calendar.calvalarr[_Calendar.evtTgt(_Calendar.EvtObj(e)).id.substring(2,_Calendar.evtTgt(_Calendar.EvtObj(e)).id.length)];
	_Calendar.$('fc').style.display='none';/*изменения с шапкой после нажатия на даты*/
	
},

/*только даты*/
f_cps:function(obj){/* раскраска календаря*/
	obj.style.background='#FFFFFF';/*заданный фон календаря*/
	obj.style.font='10px Arial';/*размер цифр календаря*/
	obj.style.color='#333333';/* цвет цифр календаря*/
	obj.style.textAlign='center';/*выравнивание цифр*/
	obj.style.textDecoration='none';
	obj.style.border='1px solid';
	obj.style.borderColor='#FFFFFF';
	obj.style.borderRadius='0';
	obj.style.cursor='pointer';
	obj.style.position='relative';
	obj.style.zIndex='2';
},

prepcalendar:function( hd, cm, cy ){
	_Calendar.now=new Date();
	sd=_Calendar.now.getDate();
	md=Math.max(cy,_Calendar.now.getFullYear());
	td=new Date();
	td.setDate(1);
	td.setFullYear(cy);
	td.setMonth(cm);
	cd=td.getDay(); // день недели
	if(cd==0)cd=6; else cd--;

	vd='';
	for(var m=0;m<12;m++) vd=vd+'<option value="'+m+'"'+(m==cm?' selected':'')+'>'+_Calendar.mn[m]+'</option>'; // цикл по месяцам

	d='';
	for(var y=cy-40;y<=md;y++)   d=d+'<option value="'+y+'"'+(y==cy?' selected':'')+'>'+y+'</option>'; // цикл по годам
	_Calendar.$('mns').innerHTML=' <select onChange="_Calendar.cmonth(this);">' + vd + '</select><select onChange="_Calendar.cyear(this);">' + d + '</select>'; // текущий месяц и год

	marr=((cy%4)==0)?_Calendar.mnl:_Calendar.mnn;

	var codes = $('#vehicles').val();
	//$('#bel_energo_companies2').empty().append($('<option>', {value: "", text: "hello kitty" + cm +  +cy}));
	//код
	var days = [];

	for (i in days)
		$('#test').append($('<option>', { text: codes}));
	for(var d=1;d<=42;d++)// цикл по всем ячейкам таблицы
	{	d=parseInt(d);
		vd=_Calendar.$ ( 'cv' + d );
		_Calendar.f_cps ( vd );
		if ((d >= (cd -(-1)))&&(d<=cd-(-marr[cm]))) {
			dd = new Date(d-cd,cm,cy);
			if(d==36)_Calendar.$("last_table_tr").style.display="";
			vd.onmouseover=_Calendar.cs_over;
			vd.onmouseout=_Calendar.cs_out;
			vd.onclick=_Calendar.cs_click;
			if (_Calendar.sccm == cm && _Calendar.sccd == (d-cd) && _Calendar.sccy == cy){ 
			  vd.style.color='#0F2345'; // сегодня
			  vd.style.fontWeight='900';
			  vd.style.fontStyle='oblique';
			  vd.style.border='1px dotted';
			  vd.style.borderColor='#577093'
			 
			 }	 
			/*else if(dd.getDay()==6||dd.getDay()==0)
				vd.style.color='#FF0000'; // выходной*/

			vd.innerHTML=d-cd;

			_Calendar.calvalarr[d]=_Calendar.addnull(d-cd,cm-(-1),cy);
		}
		else
		{
			if(d==36){_Calendar.$("last_table_tr").style.display="none"; break;}
			vd.innerHTML='&nbsp;';
			vd.onmouseover=null;
			vd.onmouseout=null;
			vd.onclick=null;
			vd.style.cursor='default';
		}
	}
	$.ajax({
		url: "daysWhenVehicleMove",
		method: "POST",
		data: {month:  cm + 1, year: cy, id: codes}
	}).done(function(data){
		$.each(data, function (i, item){
			vd=_Calendar.$ ( 'cv' + (item + cd));
			_Calendar.f_cps ( vd);{
			vd.style.color='#31B404';
			vd.style.fontWeight='bold';
			
			vd.style.border='1px outset';
			vd.style.borderRadius='10px';
			vd.style.borderColor='#E4E4E4';
			vd.innerHTML=item;
			}
			_Calendar.calvalarr[item]=_Calendar.addnull(item-cd,cm-(-1),cy);
			$('#test').append($('<option>', { text: item}));
		});
	});
},

caddm:function(){
	marr=((_Calendar.ccy%4)==0)?_Calendar.mnl:_Calendar.mnn;

	_Calendar.ccm+=1;
	if (_Calendar.ccm>=12){
		_Calendar.ccm=0;
		_Calendar.ccy++;
	}
	_Calendar.prepcalendar('',_Calendar.ccm,_Calendar.ccy);
},

csubm:function(){
	marr=((_Calendar.ccy%4)==0)?_Calendar.mnl:_Calendar.mnn;

	_Calendar.ccm-=1;
	if (_Calendar.ccm<0){
		_Calendar.ccm=11;
		_Calendar.ccy--;
	}
	_Calendar.prepcalendar('',_Calendar.ccm,_Calendar.ccy);
},

cmonth:function(t){
    _Calendar.ccm=t.options[t.selectedIndex].value;
    _Calendar.prepcalendar('',_Calendar.ccm,_Calendar.ccy);
},

cyear:function(t){
	_Calendar.ccy=t.options[t.selectedIndex].value;
	_Calendar.prepcalendar('',_Calendar.ccm,_Calendar.ccy);
},

today:function(){
	_Calendar.updobj.value=_Calendar.addnull(_Calendar.now.getDate(),_Calendar.now.getMonth()+1,_Calendar.now.getFullYear());
	_Calendar.$('fc').style.display='none';
	_Calendar.prepcalendar('',_Calendar.sccm,_Calendar.sccy);
},

addnull:function(d,m,y){
	var d0='',m0='';
	if (d<10)d0='0';
	if (m<10)m0='0';

	return ''+d0+d+'-'+m0+m+'-'+y;
}
}

_Calendar.now=n=new Date;
_Calendar.sccd=n.getDate();
_Calendar.sccm=n.getMonth();
_Calendar.sccy=n.getFullYear();
_Calendar.ccm=n.getMonth();
_Calendar.ccy=n.getFullYear();

document.write('<table id="fc" style="zIndex:2; position:absolute;border-collapse:separate;background:#FFFFFF;border:1px solid #303030;display:none;-moz-user-select:none;-khtml-user-select:none;user-select:none;" cellpadding=2>');
document.write('<tr style="zIndex:2; font:bold 13px Arial"><td style="cursor:pointer;font-size:15px" onclick="_Calendar.csubm()">&laquo;</td><td colspan="5" id="mns" align="center"></td><td align="right" style="cursor:pointer;font-size:15px" onclick="_Calendar.caddm()">&raquo;</td></tr>');
document.write('<tr style="zIndex:2; background:#8DB0E8;font:12px Arial;color:#FFFFFF"><td align=center>Пн</td><td align=center>Вт</td><td align=center>Ср</td><td align=center>Чч</td><td align=center>Пт</td><td align=center>Сб</td><td align=center>Вс</td></tr>');// дни недели
for(var kk=1;kk<=6;kk++){
	//document.write('<tr>');
	if(kk==6)
		document.write('<tr id="last_table_tr">')
	else
		document.write('<tr>');
	for(var tt=1;tt<=7;tt++){
		num=7 * (kk-1) - (-tt);
		document.write('<td id="cv' + num + '" style="zIndex:2; width:18px;height:18px">&nbsp;</td>');
	}
	document.write('</tr>');
}/* Раскраска "Сегодня"*/
document.write('<tr><td colspan="7" align="center" style="zIndex:2; cursor:pointer;font: bold 13px Arial;background:#8DB0E8" onclick="_Calendar.today()">Сегодня: '+_Calendar.addnull(_Calendar.sccd,_Calendar.sccm+1,_Calendar.sccy)+'</td></tr>');// низ календаря
document.write('</table>');

document.all?document.attachEvent('onclick',_Calendar.checkClick):document.addEventListener('click',_Calendar.checkClick,false);

_Calendar.prepcalendar('',_Calendar.ccm,_Calendar.ccy);

return _Calendar;
}();