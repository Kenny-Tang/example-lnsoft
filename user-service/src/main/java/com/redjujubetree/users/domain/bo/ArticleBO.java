package com.redjujubetree.users.domain.bo;

import com.redjujubetree.users.domain.entity.Article;
import com.redjujubetree.users.domain.entity.ColumnInfo;
import lombok.Data;

@Data
public class ArticleBO {
	private Article article;
	private ColumnInfo column;
}
