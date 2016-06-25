package com.ailk.jdbc.cache;

import com.ailk.jdbc.entity.InfoQuiz;

/**
 * 问题信息键
 * @author xugq
 *
 */
public class InfoQuizKey {
	
	private int quizId; // 问题ID
	
	public void set(InfoQuiz entity) {
		quizId = entity.getQuizId();
	}

	public int getQuizId() {
		return quizId;
	}

	public void setQuizId(int quizId) {
		this.quizId = quizId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (quizId ^ (quizId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InfoQuizKey other = (InfoQuizKey) obj;
		if (quizId != other.quizId)
			return false;
		return true;
	}
	
}
