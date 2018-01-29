$(function(){
	$('label').click(function() {
		$('label').attr('class', 'labelRadio');
		$(this).attr('class', 'labelRadioSelected');
	});
	
});