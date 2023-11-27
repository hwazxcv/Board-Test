window.addEventListener("DOMContentLoaded", function() {
/* 체크박스 전체 선택 처리 방식*/
const checkalls = document.getElementsByClassName("checkall");
for( const el of checkalls){
        el.addEventListener("click" , function(){
            const name = this.dataset.targetName;
            const chks = document.getElementsByName(name);
            for( const el of chks){
                el.checked = this.checked;
            }
        });
}



/*양식 처리 클래스 (삭제와 수정) */
    const formActions = document.getElementsByClassName("form-action");
    for (const el of formActions) {
        el.addEventListener("click", function() {
            const mode = this.dataset.mode;
            const formName = this.dataset.formName;

            const frm = document.forms[formName];
            if (!frm._method) return;

            frm._method.value = mode == 'delete' ? 'DELETE' : 'PATCH';
            const confirmMsg = `정말 ${mode == 'delete' ? '삭제':'수정'} 하시겠습니까?`;
            if (confirm(confirmMsg)) {
                frm.submit();
            }
        });
    }
});