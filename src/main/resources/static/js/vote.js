/*jshint esversion: 8 */
/* 투표하기 */
async function sendVote(voteInfoId) {

    let vote = [];
    let voteListDiv = document.getElementsByClassName('voteList');
    for (let voteDiv of voteListDiv) {
        let voteId = voteDiv.querySelector('#voteListId').value;
        let candidateItemsDiv = voteDiv.querySelectorAll('.candidate-items .candidate-item');
        let candidate = [];

        let count = 0;
        for (let candidateItemDiv of candidateItemsDiv) {
            let candidateId = candidateItemDiv.querySelector('#candidateId').value;
            let candidateSequenceNumber = candidateItemDiv.querySelector('#candidateSequenceNumber')
                .value;
            let candidateItem = candidateItemDiv.querySelector(`input[name=voteList${voteId}]`);
            let value = 0;
            if (candidateItem.checked) {
                value = 1;
                count++;
            }
            vote.push({
                voteInfoId,
                voteId,
                candidateSequenceNumber,
                candidateId,
                value
            });
        }
        if (count != 1) {
            alert("투표 선택은 1개만 하세요");
            return;
        }
    }

    let response = await fetch(`/api/v1/vote/voting`, {
        method: 'POST',
        headers: {
            'Content-type': 'application/json'
        },
        body: JSON.stringify(vote)
    });


    if (response && response.ok) {
        alert('투표가 성공적으로 마쳤습니다');
        window.location.href = '/vote/success';
    } else {
        alert('전송실패');
    }
}


/* 인증 */
async function signinByLink() {
    let name = document.getElementById('name').value;
    let phone1 = document.getElementById('phone1').value;
    let phone2 = document.getElementById('phone2').value;
    let phone3 = document.getElementById('phone3').value;
    let phone = phone1 + phone2 + phone3;
    let uid = document.getElementById('uid').value;
    let url = `/api/v1/vote/sign/`;
    let response = await fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            name,
            phone,
            uid,
        })
    });

    if (response && response.ok) {
        window.location.replace(`/vote/voting`)
    } else {
        let error = document.getElementById('error');
        let errorObject = (await response.json()).message;
        error.innerText = errorObject;
        error.style.display = 'block';
    }
}

/* 투표 만들기 & 수정 */
function addVote(e) {
    let votes = document.getElementsByClassName('votes')[0];
    let appendDiv = document.getElementsByClassName('vote-template')[0].childNodes[1];
    votes.appendChild(appendDiv.cloneNode(true));
}

function removeVote(value) {
    value.closest('.vote-item').remove();
}

function addCandidate(value) {
    let appendDiv = document.getElementsByClassName('candidate-template')[0].childNodes[1];
    value.closest('.vote-item').querySelector('.candidates').append(appendDiv.cloneNode(true));
}

function removeCandidate(value) {
    value.closest('.candidate-item').remove();
}

function checkForm() {
    let form = document.getElementById('voteForm');
    let voteInfoTitle = form.querySelector('#title');
    if (!voteInfoTitle.value) {
        voteInfoTitle.scrollIntoView();
        return false;
    }
    let votesDiv = form.querySelector('.votes');
    for (let voteItem of votesDiv.querySelectorAll('.vote-item')) {
        let voteName = voteItem.querySelector('#title').value;
        let candidatesDiv = voteItem.querySelector('.candidates');
        for (let candidateItem of candidatesDiv.querySelectorAll('.candidate-item')) {
            let candidateName = candidateItem.querySelector('#candidate').value;
        }
    }

    return true;
}

function sendForm(applyId) {
    if (!checkForm(this)) {
        return;
    }
    if (!confirm('등록하시겠습니까?')) {
        return;
    }

    let votes = getVotes(applyId);

    if(!votes){
        return;
    }

    fetch('/api/v1/vote/making', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(votes)
    }).then((response) => {

        alert("등록 되었습니다");
        window.location.replace("/apply");
    }).catch((response) => {
        alert("등록이 실패하였습니다");
    });
}

function modifyForm(applyId) {
    if (!checkForm(this)) {
        return;
    }
    if (!confirm('등록하시겠습니까?')) {
        return;
    }


    let votes = getVotes(applyId);
    if (!votes) {
        return;
    }
    fetch('/api/v1/vote/making', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(votes)
    }).then((response) => {

        alert("수정 되었습니다");
        window.location.replace("/apply");
    }).catch((response) => {
        alert("수정에 실패하였습니다");
    });
}

function getVotes(applyId) {

    let form = document.getElementById('voteForm');
    let voteInfoTitle = form.querySelector('#title').value;
    let voteInfoDesc = form.querySelector('#description').value;
    let vote = [];

    let votesDiv = form.querySelector('.votes');
    let voteNum = 1;
    let voteList = votesDiv.querySelectorAll('.vote-item');

    if (voteList.length <= 0) {
        alert('투표 최소 한개 이상 필요합니다.');
        return;
    }
    for (let voteItem of voteList) {
        let voteSeqNum = voteNum++;
        let voteSelNum = 1; //나중에 추가
        let voteName = voteItem.querySelector('#title').value;
        let voteElecNum = 1; //나중에 추가
        let candidatesDiv = voteItem.querySelector('.candidates');
        let candidateNum = 1;
        let candidate = [];
        let candidateList = candidatesDiv.querySelectorAll('.candidate-item');

        if (candidateList.length <= 1) {
            alert('후보자 최소 두 명 이상 필요합니다.');
            return;
        }
        for (let candidateItem of candidateList) {
            let candidateSeqNo = candidateNum++;
            let candidateName = candidateItem.querySelector('#candidate').value;
            let candidateDesc = candidateItem.querySelector('#candidateDesc').value;
            candidate.push({
                voteSelNum,
                candidateSeqNo,
                candidateName,
                candidateDesc
            });
        }
        vote.push({
            voteSeqNum,
            voteSelNum,
            voteName,
            voteElecNum,
            candidate
        });

    }
    return {
        applyId,
        voteInfoTitle,
        voteInfoDesc,
        vote
    };
}