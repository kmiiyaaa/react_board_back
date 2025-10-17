import api from "../api/axiosConfig";
import { useState } from "react";

function CommenetList({ comments, user, loadComments }) {
  //comments 댓글리스트 : boatdList에서 받아온다
  // user : app -> boardDetail에서 받아온다

  const [editCommentId, setEditCommentId] = useState(null);
  const [editCommentContent, setEditCommentContent] = useState("");

  //댓글 삭제 함수
  const handleCommentDelete = async (commentId) => {
    if (!window.confirm("정말 삭제하시겠습니까?")) {
      return;
    }
    try {
      await api.delete(`/api/comments/${commentId}`);
      alert("댓글 삭제 성공!");
      loadComments(); //갱신된 댓글 리스트 다시 로딩
    } catch (err) {
      console.error(err);
      alert("삭제 권한이 없거나 삭제할 수 없는 댓글입니다.");
    }
  };

  //댓글 수정 이벤트 함수 -> 백엔드 수정 요청
  const handleCommentUpdate = async (commentId) => {
    try {
      await api.put(`/api/comments/${commentId}`, {
        content: editCommentContent, // content -> 수정한 내용
      });
      setEditCommentId(null);
      setEditCommentContent("");
      loadComments(); //댓글 리스트 갱신
    } catch (err) {
      alert("댓글 수정 실패!");
    }
  };

  //댓글 수정 여부 확인
  const handleCommentEdit = (comment) => {
    setEditCommentId(comment.id);
    setEditCommentContent(comment.content);
    //EditingCommentContent->수정할 내용으로 저장
  };

  //날짜 format 함수 -> 날짜, 시간 출력
  const commentFormatDate = (dateString) => {
    return new Date(dateString).toLocaleString();
  };

  return (
    <ul className="comment-list">
      {comments.length === 0 && <p>아직 등록된 댓글이 없습니다.</p>}
      {comments.map((c) => (
        <li key={c.id} className="comment-item">
          <div className="comment-header">
            <span className="comment-author">{c.author.username}</span>
            <span className="comment-date">
              {commentFormatDate(c.createDate)}
            </span>
          </div>

          {editCommentId === c.id ? (
            /* 댓글 수정 섹션 시작! */
            <>
              <textarea
                value={editCommentContent}
                onChange={(e) => setEditCommentContent(e.target.value)}
              />
              <button
                className="comment-save"
                onClick={() => handleCommentUpdate(c.id)}
              >
                저장
              </button>
              <button
                className="comment-cancel"
                onClick={() => setEditCommentId(null)}
              >
                취소
              </button>
            </>
          ) : (
            /* 댓글 읽기 섹션 */
            <>
              <div className="comment-content">{c.content}</div>

              <div className="button-group">
                {/* 로그인한 유저 본인이 쓴 댓글만 삭제 수정 가능 */}
                {user === c.author.username && (
                  <>
                    <button
                      className="edit-button"
                      onClick={() => handleCommentEdit(c)}
                    >
                      수정
                    </button>
                    <button
                      className="delete-button"
                      onClick={() => handleCommentDelete(c.id)}
                    >
                      삭제
                    </button>
                  </>
                )}
              </div>
            </>
            /* 댓글 읽기 섹션 끝! */
          )}
        </li>
      ))}
    </ul>
  );
}

export default CommenetList;
