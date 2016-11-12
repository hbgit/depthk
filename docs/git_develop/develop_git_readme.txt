# 
.gitignore 

# Excluding files from git archive exports using gitattributes

.gitattributes

# Git branching model

-> master
# We consider origin/master to be the main branch where the source code of HEAD always reflects a production-ready state.

### Feature branches 

May branch off from:
develop
Must merge back into:
develop
Branch naming convention:
anything except master, develop, release-*, or hotfix-*

# How git actions in the branches could be see at http://nvie.com/posts/a-successful-git-branching-model

-> develop

We consider origin/develop to be the main branch where the source code of HEAD always reflects a state with the latest 
delivered development changes for the next release. Some would call this the “integration branch”. This is where any 
automatic nightly builds are built from.

When the source code in the develop branch reaches a stable point and is ready to be released, all of the changes should be 
merged back into master somehow and then tagged with a release number.

-> release-*

Release branches support preparation of a new production release. They allow for last-minute dotting of i’s and crossing t’s. 
Furthermore, they allow for minor bug fixes and preparing meta-data for a release (version number, build dates, etc.). 
By doing all of this work on a release branch, the develop branch is cleared to receive features for the next big release.
