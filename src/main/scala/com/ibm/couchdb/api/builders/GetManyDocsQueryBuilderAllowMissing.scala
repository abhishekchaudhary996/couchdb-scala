/*
 * Copyright 2015 IBM Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibm.couchdb.api.builders

import com.ibm.couchdb._
import com.ibm.couchdb.core.Client
import org.http4s.Status
import upickle.default.Aliases.{R, W}
import upickle.default.write

import scalaz.concurrent.Task

case class GetManyDocsQueryBuilderAllowMissing(client: Client,
                                               db: String,
                                               params: Map[String, String] = Map.empty[String, String]) {

  def conflicts(conflicts: Boolean = true): GetManyDocsQueryBuilderAllowMissing = {
    set("conflicts", conflicts)
  }

  def descending(descending: Boolean = true): GetManyDocsQueryBuilderAllowMissing = {
    set("descending", descending)
  }

  def endKey[K: W](endKey: K): GetManyDocsQueryBuilderAllowMissing = {
    set("endkey", write(endKey))
  }

  def endKeyDocId(endKeyDocId: String): GetManyDocsQueryBuilderAllowMissing = {
    set("endkey_docid", endKeyDocId)
  }

  private def includeDocs(includeDocs: Boolean = true): GetManyDocsQueryBuilderAllowMissing = {
    set("include_docs", includeDocs)
  }

  def inclusiveEnd(inclusiveEnd: Boolean = true): GetManyDocsQueryBuilderAllowMissing = {
    set("inclusive_end", inclusiveEnd)
  }

  def key[K: W](key: K): GetManyDocsQueryBuilderAllowMissing = {
    set("key", write(key))
  }

  def limit(limit: Int): GetManyDocsQueryBuilderAllowMissing = {
    set("limit", limit)
  }

  def skip(skip: Int): GetManyDocsQueryBuilderAllowMissing = {
    set("skip", skip)
  }

  def stale(stale: String): GetManyDocsQueryBuilderAllowMissing = {
    set("stale", stale)
  }

  def startKey[K: W](startKey: K): GetManyDocsQueryBuilderAllowMissing = {
    set("startkey", write(startKey))
  }

  def startKeyDocId(startKeyDocId: String): GetManyDocsQueryBuilderAllowMissing = {
    set("startkey_docid", startKeyDocId)
  }

  def updateSeq(updateSeq: Boolean = true): GetManyDocsQueryBuilderAllowMissing = {
    set("update_seq", updateSeq)
  }

  private def set(key: String, value: String): GetManyDocsQueryBuilderAllowMissing = {
    copy(params = params.updated(key, value))
  }

  private def set(key: String, value: Any): GetManyDocsQueryBuilderAllowMissing = {
    set(key, value.toString)
  }

  def query(ids: Seq[String]): Task[CouchKeyValsIncludesMissing[String, CouchDocRev]] = {
    if (ids.isEmpty)
      Res.Error("not_found", "No IDs specified").toTask[CouchKeyValsIncludesMissing[String, CouchDocRev]]
    else
      client.post[Req.DocKeys[String], CouchKeyValsIncludesMissing[String, CouchDocRev]](
                                                                                   s"/$db/_all_docs",
                                                                                   Status.Ok,
                                                                                   Req.DocKeys(ids),
                                                                                   params.toSeq)
  }

  def queryIncludeDocs[D: R](ids: Seq[String]): Task[CouchDocsIncludesMissing[String, CouchDocRev, D]] = {
    if (ids.isEmpty)
      Res.Error("not_found", "No IDs specified").toTask[CouchDocsIncludesMissing[String, CouchDocRev, D]]
    else
      client.post[Req.DocKeys[String], CouchDocsIncludesMissing[String, CouchDocRev, D]](
        s"/$db/_all_docs",
        Status.Ok,
        Req.DocKeys(ids),
        includeDocs().params.toSeq)
  }
}
