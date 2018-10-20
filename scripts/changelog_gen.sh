#!/usr/bin/env bash

SAVEIFS=$IFS
IFS=$'\n'

GIT_REPO='https://github.com/sendgrid/java-http-client'
TITLE_REGEX='<span class="js-issue-title">([^<]+)</span>'
print_pr_info() {
   url="${GIT_REPO}/pull/${1}"
   html=$(curl -# -L "$url" 2> '/dev/null')
   if [[ $html =~ $TITLE_REGEX ]]; then
      title=$(echo ${BASH_REMATCH[1]} | xargs)
   fi

   echo "- [Pull #${1}](${url}): ${title} (Thanks [${2}](https://github.com/${2})!)" >> changelog_tmp.md
}

PR_REGEX='Merge pull request #([0-9]+) from (.+)/.+'
find_pull_requests() {
   pullRequests=$(git log --merges --grep="Merge pull request" --oneline $1...$2)
   for pr in $pullRequests; do
      if [[ $pr =~ $PR_REGEX ]]; then
         prNumber=${BASH_REMATCH[1]}
         author=${BASH_REMATCH[2]}

         print_pr_info $prNumber $author
      fi
   done
}

check_version_changes() {
   IFS='.'
   prev=$1
   current=$2

   set -- $prev
   prevMajor=$1
   prevMinor=$2
   prevPatch=$3

   set -- $current
   currMajor=$1
   currMinor=$2
   currPatch=$3

   IFS=$'\n'

   if [[ $currMajor -ne $prevMajor ]]; then
      echo "### BREAKING Change" >> changelog_tmp.md
   elif [[ $currMinor -ne $prevMinor ]]; then
      echo "### Added" >> changelog_tmp.md
   elif [[ $currPatch -ne $prevPatch ]]; then
      echo "### Fix" >> changelog_tmp.md
   fi
}

generate_changelog() {
   head -n 5 CHANGELOG.md > changelog_tmp.md
   tagDate=$(git log -1 --date=short --format="%ad" $2);

   echo "## [${2:1}] - ${tagDate}" >> changelog_tmp.md
   check_version_changes $1 $2
   find_pull_requests $1 $2

   tail -n +5 CHANGELOG.md >> changelog_tmp.md
   mv changelog_tmp.md CHANGELOG.md
}

TAGS=$(git tag | tail -n 2)

prevTag=
for currentTag in $TAGS; do
   if [ -z $prevTag ]; then
      prevTag=$currentTag
      continue
   fi

   generate_changelog $prevTag $currentTag
   prevTag=$currentTag
done

IFS=$SAVEIFS
