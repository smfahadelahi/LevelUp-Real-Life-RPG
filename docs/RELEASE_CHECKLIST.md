# Release Checklist

## Before a public beta
- [ ] Run the Android CI workflow and resolve all build/test failures.
- [ ] Install on at least one API 26+ device and a current Android device.
- [ ] Test quest completion, duplicate reward prevention, custom quest creation, and next-day reset.
- [ ] Test habit check-ins, unchecking, and next-day history.
- [ ] Test database migration from the previous schema.
- [ ] Review the launcher icon, create store screenshots, store listing, and support contact.
- [ ] Add an in-app privacy page and a public privacy policy URL.
- [ ] Review accessibility, font scaling, TalkBack labels, and color contrast.
- [ ] Create a release signing key and store it securely outside GitHub.
- [ ] Configure Play App Signing and internal testing in Google Play Console.

## Not included in the current MVP
Cloud accounts, social features, public leaderboards, paid subscriptions, advertising, analytics, and AI-generated quests.
